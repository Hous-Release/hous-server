package hous.api.service.user;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import hous.api.service.user.dto.request.CreateUserRequestDto;
import hous.api.service.user.dto.request.DeleteUserRequestDto;
import hous.core.domain.feedback.FeedbackType;
import hous.core.domain.room.Room;
import hous.core.domain.room.mysql.RoomRepository;
import hous.core.domain.user.User;
import hous.core.domain.user.UserSocialType;
import hous.core.domain.user.mysql.UserRepository;

@SpringBootTest
@ActiveProfiles(value = "local")
@Transactional
public class UserServiceTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private UserService userService;

	// 정책상 방에 들어가지 않을 경우에만 사용자 삭제가 가능
	@Test
	@DisplayName("방에 들어가지 않은 사용자 삭제 성공")
	public void delete_user_by_not_join_room_success() {
		// given
		CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.of(
			"socialId1", UserSocialType.KAKAO, "fcmToken1", "nickname1", "2022-01-01", true);
		Long userId = userService.registerUser(createUserRequestDto);
		DeleteUserRequestDto deleteUserRequestDto = DeleteUserRequestDto.of(FeedbackType.NO, "");

		// when
		userService.deleteUser(deleteUserRequestDto, userId);

		// then
		List<User> users = userRepository.findAll();
		assertThat(users).isEmpty();
		List<Room> rooms = roomRepository.findAll();
		assertThat(rooms).isEmpty();
	}
}
