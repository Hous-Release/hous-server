package hous.api.service.notification.dto.response;

import hous.common.util.DateUtils;
import hous.core.domain.common.collection.ScrollPaginationCollection;
import hous.core.domain.notification.Notification;
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

    public static NotificationsInfoResponse of(ScrollPaginationCollection<Notification> notificationsScroll, long totalElements) {
        if (notificationsScroll.isLastScroll()) {
            return NotificationsInfoResponse.newLastScroll(notificationsScroll.getCurrentScrollItems(), totalElements);
        }
        return NotificationsInfoResponse.newScrollHasNext(notificationsScroll.getCurrentScrollItems(), totalElements, notificationsScroll.getNextCursor().getId());
    }

    private static NotificationsInfoResponse newLastScroll(List<Notification> notificationsScroll, long totalElements) {
        return newScrollHasNext(notificationsScroll, totalElements, LAST_CURSOR);
    }

    private static NotificationsInfoResponse newScrollHasNext(List<Notification> notificationsScroll, long totalElements, long nextCursor) {
        return new NotificationsInfoResponse(getContents(notificationsScroll, DateUtils.todayLocalDateTime()), totalElements, nextCursor);
    }

    private static List<NotificationInfo> getContents(List<Notification> notificationsScroll, LocalDateTime now) {
        return notificationsScroll.stream()
                .map(notification -> NotificationInfo.of(notification, now))
                .collect(Collectors.toList());
    }
}
