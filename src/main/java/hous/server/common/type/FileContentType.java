package hous.server.common.type;

import hous.server.common.exception.ValidationException;
import lombok.Getter;

import static hous.server.common.exception.ErrorCode.FORBIDDEN_FILE_TYPE_EXCEPTION;

@Getter
public enum FileContentType {

    IMAGE("image"),
    ;

    private final String prefix;

    private FileContentType(String prefix) {
        this.prefix = prefix;
    }

    public void validateAvailableContentType(String contentType) {
        if (contentType != null && contentType.contains(SEPARATOR) && prefix.equals(getContentTypePrefix(contentType))) {
            return;
        }
        throw new ValidationException(String.format("허용되지 않은 파일 형식 (%s) 입니다", contentType), FORBIDDEN_FILE_TYPE_EXCEPTION);
    }

    private static String getContentTypePrefix(String contentType) {
        return contentType.split(SEPARATOR)[0];
    }

    private static final String SEPARATOR = "/";
}
