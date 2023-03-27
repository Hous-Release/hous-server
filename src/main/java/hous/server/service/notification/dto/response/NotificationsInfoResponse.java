package hous.server.service.notification.dto.response;

import hous.server.common.util.DateUtils;
import hous.server.domain.common.collection.ScrollPaginationCollection;
import hous.server.domain.notification.Notification;
import hous.server.domain.notification.mongo.NotificationRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationsInfoResponse {
    private static final long LAST_CURSOR = -1L;

    private List<NotificationInfo> contents = new ArrayList<>();
    private long totalElements;
    private long nextCursor;

    private NotificationsInfoResponse(List<NotificationInfo> contents, long totalElements, long nextCursor) {
        this.contents = contents;
        this.totalElements = totalElements;
        this.nextCursor = nextCursor;
    }

    public static NotificationsInfoResponse of(NotificationRepository notificationRepository, ScrollPaginationCollection<Notification> notificationsScroll, long totalElements) {
        if (notificationsScroll.isLastScroll()) {
            return NotificationsInfoResponse.newLastScroll(notificationRepository, notificationsScroll.getCurrentScrollItems(), totalElements);
        }
        return NotificationsInfoResponse.newScrollHasNext(notificationRepository, notificationsScroll.getCurrentScrollItems(), totalElements, notificationsScroll.getNextCursor().getId());
    }

    private static NotificationsInfoResponse newLastScroll(NotificationRepository notificationRepository, List<Notification> notificationsScroll, long totalElements) {
        return newScrollHasNext(notificationRepository, notificationsScroll, totalElements, LAST_CURSOR);
    }

    private static NotificationsInfoResponse newScrollHasNext(NotificationRepository notificationRepository, List<Notification> notificationsScroll, long totalElements, long nextCursor) {
        return new NotificationsInfoResponse(getContents(notificationRepository, notificationsScroll, DateUtils.todayLocalDateTime()), totalElements, nextCursor);
    }

    private static List<NotificationInfo> getContents(NotificationRepository notificationRepository, List<Notification> notificationsScroll, LocalDateTime now) {
        return notificationsScroll.stream()
                .map(notification -> NotificationInfo.of(notificationRepository, notification, now))
                .collect(Collectors.toList());
    }
}
