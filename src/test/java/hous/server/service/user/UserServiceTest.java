package hous.server.service.user;

import hous.server.domain.feedback.FeedbackType;
import hous.server.domain.room.Room;
import hous.server.domain.room.mysql.RoomRepository;
import hous.server.domain.user.User;
import hous.server.domain.user.UserSocialType;
import hous.server.domain.user.mysql.UserRepository;
import hous.server.service.user.dto.request.CreateUserRequestDto;
import hous.server.service.user.dto.request.DeleteUserRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
