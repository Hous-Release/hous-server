package hous.api.service.todo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import hous.api.service.room.RoomService;
import hous.api.service.room.dto.request.SetRoomNameRequestDto;
import hous.api.service.room.dto.response.RoomInfoResponse;
import hous.api.service.todo.dto.request.TodoInfoRequestDto;
import hous.api.service.user.UserService;
import hous.api.service.user.dto.request.CreateUserRequestDto;
import hous.common.exception.ConflictException;
import hous.common.exception.NotFoundException;
import hous.core.domain.todo.DayOfWeek;
import hous.core.domain.todo.Redo;
import hous.core.domain.todo.Take;
import hous.core.domain.todo.Todo;
import hous.core.domain.todo.mysql.RedoRepository;
import hous.core.domain.todo.mysql.TakeRepository;
import hous.core.domain.todo.mysql.TodoRepository;
import hous.core.domain.user.User;
import hous.core.domain.user.UserSocialType;
import hous.core.domain.user.mysql.UserRepository;

@SpringBootTest
@ActiveProfiles(value = "local")
public class TodoServiceTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RedoRepository redoRepository;

	@Autowired
	private TakeRepository takeRepository;

	@Autowired
	private TodoRepository todoRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private RoomService roomService;

	@Autowired
	private TodoService todoService;

	@Test
	@DisplayName("todo 동시에 2개 추가해도 성공")
	public void create_two_todos_at_once_success() throws InterruptedException {

		// given
		CreateUserRequestDto createUserRequestDto1 = CreateUserRequestDto.of(
			"socialId1", UserSocialType.KAKAO, "fcmToken1", "nickname1", "2022-01-01", true);
		CreateUserRequestDto createUserRequestDto2 = CreateUserRequestDto.of(
			"socialId2", UserSocialType.KAKAO, "fcmToken2", "nickname2", "2022-01-01", true);
		Long userId1 = userService.registerUser(createUserRequestDto1);
		Long userId2 = userService.registerUser(createUserRequestDto2);
		User user1 = userRepository.findUserById(userId1);
		User user2 = userRepository.findUserById(userId2);

		SetRoomNameRequestDto setRoomNameRequestDto = SetRoomNameRequestDto.of("room1");
		RoomInfoResponse roomInfoResponse = roomService.createRoom(setRoomNameRequestDto, userId1);
		roomService.joinRoom(roomInfoResponse.getRoomId(), userId2);

		TodoInfoRequestDto todoInfoRequestDto1 = TodoInfoRequestDto.of("todo1", true,
			List.of(TodoInfoRequestDto.TodoUser.builder()
					.onboardingId(user1.getOnboarding().getId())
					.dayOfWeeks(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY))
					.build(),
				TodoInfoRequestDto.TodoUser.builder()
					.onboardingId(user2.getOnboarding().getId())
					.dayOfWeeks(List.of(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY))
					.build()));
		TodoInfoRequestDto todoInfoRequestDto2 = TodoInfoRequestDto.of("todo1", true,
			List.of(TodoInfoRequestDto.TodoUser.builder()
					.onboardingId(user1.getOnboarding().getId())
					.dayOfWeeks(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY))
					.build(),
				TodoInfoRequestDto.TodoUser.builder()
					.onboardingId(user2.getOnboarding().getId())
					.dayOfWeeks(List.of(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY))
					.build()));

		// when
		ExecutorService executorService = Executors.newFixedThreadPool(2);
		CountDownLatch countDownLatch = new CountDownLatch(2);

		for (int i = 1; i <= 2; i++) {
			int finalI = i;
			executorService.execute(() -> {
				switch (finalI) {
					case 1:
						todoService.createTodo(todoInfoRequestDto1, userId1);
						break;
					case 2:
						todoService.createTodo(todoInfoRequestDto2, userId2);
						break;
				}
				countDownLatch.countDown();
			});
		}

		countDownLatch.await();

		// then
		List<Todo> todos = todoRepository.findAll();
		assertThat(todos.size()).isEqualTo(2);
	}

	@Test
	@DisplayName("이미 존재하는 todo 와 같은 이름을 가진 todo 추가할 경우 409 예외 발생")
	@Transactional
	public void create_duplicate_todo_name_throw_by_conflict_exception() {
		// given
		CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.of(
			"socialId1", UserSocialType.KAKAO, "fcmToken1", "nickname1", "2022-01-01", true);
		Long userId = userService.registerUser(createUserRequestDto);
		User user = userRepository.findUserById(userId);

		SetRoomNameRequestDto setRoomNameRequestDto = SetRoomNameRequestDto.of("room1");
		Long roomId = roomService.createRoom(setRoomNameRequestDto, userId).getRoomId();

		TodoInfoRequestDto todoInfoRequestDto = TodoInfoRequestDto.of("todo1", true,
			List.of(TodoInfoRequestDto.TodoUser.builder()
				.onboardingId(user.getOnboarding().getId())
				.dayOfWeeks(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY))
				.build()));
		todoService.createTodo(todoInfoRequestDto, userId);

		// when, then
		List<Todo> todos = todoRepository.findAll();
		List<Take> takes = takeRepository.findAll();
		List<Redo> redos = redoRepository.findAll();
		assertThat(todos.size()).isEqualTo(1);
		assertThat(takes.size()).isEqualTo(1);
		assertThat(redos.size()).isEqualTo(3);
		String matchedExceptionMessage = String.format("방 (%s) 에 이미 존재하는 todo (%s) 입니다.", roomId, "todo1");
		assertThatThrownBy(() -> {
			todoService.createTodo(todoInfoRequestDto, userId);
		}).isInstanceOf(ConflictException.class)
			.hasMessageContaining(matchedExceptionMessage);
	}

	@Test
	@DisplayName("todo 삭제 성공")
	@Transactional
	public void delete_todo_success() {
		// given
		CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.of(
			"socialId1", UserSocialType.KAKAO, "fcmToken1", "nickname1", "2022-01-01", true);
		Long userId = userService.registerUser(createUserRequestDto);
		User user = userRepository.findUserById(userId);

		SetRoomNameRequestDto setRoomNameRequestDto = SetRoomNameRequestDto.of("room1");
		roomService.createRoom(setRoomNameRequestDto, userId);

		TodoInfoRequestDto todoInfoRequestDto = TodoInfoRequestDto.of("todo1", true,
			List.of(TodoInfoRequestDto.TodoUser.builder()
				.onboardingId(user.getOnboarding().getId())
				.dayOfWeeks(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY))
				.build()));
		todoService.createTodo(todoInfoRequestDto, userId);

		// when
		Long todoId = todoRepository.findAll().get(0).getId();
		todoService.deleteTodo(todoId, userId);

		// then
		List<Todo> todos = todoRepository.findAll();
		List<Take> takes = takeRepository.findAll();
		List<Redo> redos = redoRepository.findAll();
		assertThat(todos).isEmpty();
		assertThat(takes).isEmpty();
		assertThat(redos).isEmpty();
	}

	@Test
	@DisplayName("todo 삭제 시 존재하지 않는 todo_id인 경우 404 예외 발생")
	@Transactional
	public void delete_rules_throw_by_not_found_exception() {
		// given
		CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.of(
			"socialId1", UserSocialType.KAKAO, "fcmToken1", "nickname1", "2022-01-01", true);
		Long userId = userService.registerUser(createUserRequestDto);
		User user = userRepository.findUserById(userId);

		SetRoomNameRequestDto setRoomNameRequestDto = SetRoomNameRequestDto.of("room1");
		roomService.createRoom(setRoomNameRequestDto, userId);

		TodoInfoRequestDto todoInfoRequestDto = TodoInfoRequestDto.of("todo1", true,
			List.of(TodoInfoRequestDto.TodoUser.builder()
				.onboardingId(user.getOnboarding().getId())
				.dayOfWeeks(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY))
				.build()));
		todoService.createTodo(todoInfoRequestDto, userId);

		// when, then
		Long unExistsTodoId = todoRepository.findAll().size() + 1L;
		String matchedExceptionMessage = String.format("존재하지 않는 todo (%s) 입니다", unExistsTodoId);
		assertThatThrownBy(() -> {
			todoService.deleteTodo(2L, userId);
		}).isInstanceOf(NotFoundException.class)
			.hasMessageContaining(matchedExceptionMessage);
	}
}
