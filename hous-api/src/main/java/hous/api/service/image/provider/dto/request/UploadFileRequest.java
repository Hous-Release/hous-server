package hous.api.service.image.provider.dto.request;

import hous.common.type.FileType;

public interface UploadFileRequest {

	FileType getType();

	default void validateAvailableContentType(String contentType) {
		getType().validateAvailableContentType(contentType);
	}

	default String getFileNameWithBucketDirectory(String originalFileName) {
		return getType().createUniqueFileNameWithExtension(originalFileName);
	}
}
