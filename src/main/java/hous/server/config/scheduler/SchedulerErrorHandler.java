package hous.server.config.scheduler;

import hous.server.common.exception.HousException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.ErrorHandler;

@Slf4j
public class SchedulerErrorHandler implements ErrorHandler {

    @Override
    public void handleError(@NotNull Throwable throwable) {
        if (throwable instanceof HousException) {
            HousException exception = (HousException) throwable;
            if (exception.getStatus() >= 400 && exception.getStatus() < 500) {
                log.warn(exception.getMessage(), exception);
            } else {
                log.error(exception.getMessage(), exception);
            }
        } else {
            Exception exception = (Exception) throwable;
            log.error(exception.getMessage(), exception);
        }
    }
}
