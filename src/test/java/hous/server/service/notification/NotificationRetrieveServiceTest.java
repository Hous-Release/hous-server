package hous.server.service.notification;

import hous.server.domain.room.Room;
import hous.server.domain.todo.Todo;
import hous.server.domain.user.User;
import hous.server.domain.user.UserSocialType;
import hous.server.domain.user.mysql.UserRepository;
import hous.server.service.notification.dto.response.NotificationsInfoResponse;
import hous.server.service.user.UserService;
import hous.server.service.user.dto.request.CreateUserRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles(value = "local")
@Transactional
public class NotificationRetrieveServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRetrieveService notificationRetrieveService;

    @Test
    @DisplayName("스크롤 페이지네이션으로 알림 목록 조회 성공")
    public void get_notifications_info_by_scroll_pagination_success() {
        // given
        CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.of(
                "socialId1", UserSocialType.KAKAO, "fcmToken1", "nickname1", "2022-01-01", true);
        Long userId = userService.registerUser(createUserRequestDto);
        User user = userRepository.findUserById(userId);
        Room room = Room.newInstance(user.getOnboarding(), "room1", "code1");
        Todo todo = Todo.newInstance(room, "todo1", true);
        notificationService.sendNewTodoNotification(user, todo, true);
        notificationService.sendNewTodoNotification(user, todo, true);
        notificationService.sendNewTodoNotification(user, todo, true);

        // when
        NotificationsInfoResponse response1 = notificationRetrieveService.getNotificationsInfo(1, Long.MAX_VALUE, userId);
        NotificationsInfoResponse response2 = notificationRetrieveService.getNotificationsInfo(1, response1.getNextCursor(), userId);
        NotificationsInfoResponse response3 = notificationRetrieveService.getNotificationsInfo(1, response2.getNextCursor(), userId);

        NotificationsInfoResponse response4 = notificationRetrieveService.getNotificationsInfo(1, Long.MAX_VALUE, userId);
        NotificationsInfoResponse response5 = notificationRetrieveService.getNotificationsInfo(1, response4.getNextCursor(), userId);
        NotificationsInfoResponse response6 = notificationRetrieveService.getNotificationsInfo(1, response5.getNextCursor(), userId);

        // then
        assertThat(response1.getContents()).hasSize(1);
        assertThat(response1.getContents().get(0).isRead()).isFalse();
        assertThat(response1.getNextCursor()).isEqualTo(3);
        assertThat(response2.getContents()).hasSize(1);
        assertThat(response2.getContents().get(0).isRead()).isFalse();
        assertThat(response2.getNextCursor()).isEqualTo(2);
        assertThat(response3.getContents()).hasSize(1);
        assertThat(response3.getContents().get(0).isRead()).isFalse();
        assertThat(response3.getNextCursor()).isEqualTo(-1);

        assertThat(response4.getContents()).hasSize(1);
        assertThat(response4.getContents().get(0).isRead()).isTrue();
        assertThat(response4.getNextCursor()).isEqualTo(3);
        assertThat(response5.getContents()).hasSize(1);
        assertThat(response5.getContents().get(0).isRead()).isTrue();
        assertThat(response5.getNextCursor()).isEqualTo(2);
        assertThat(response6.getContents()).hasSize(1);
        assertThat(response6.getContents().get(0).isRead()).isTrue();
        assertThat(response6.getNextCursor()).isEqualTo(-1);
    }
}
