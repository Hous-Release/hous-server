package hous.api.service.image.provider;

import static hous.common.exception.ErrorCode.*;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import hous.api.service.image.client.S3FileStorageClient;
import hous.api.service.image.dto.UploadResponseDto;
import hous.api.service.image.provider.dto.request.UploadFileRequest;
import hous.common.exception.ValidationException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class S3Provider {

	private static final int MAX_FILE_SIZE = 518400; // 720 * 720

	private final S3FileStorageClient fileStorageClient;

	public UploadResponseDto uploadFile(UploadFileRequest request, MultipartFile file) {
		request.validateAvailableContentType(file.getContentType());
		String fileName = request.getFileNameWithBucketDirectory(file.getOriginalFilename());
		String originalFileName = request.getCreateOriginalFileNameWithDate(file.getOriginalFilename());
		validateImageMaxSize(file);
		fileStorageClient.uploadFile(file, fileName);
		return UploadResponseDto.of(originalFileName, fileStorageClient.getFileUrl(fileName));
	}

	public void deleteFile(String fileName) {
		if (fileName != null) {
			String[] image = fileName.split(".com/");
			fileStorageClient.deleteFile(image[1]);
		}
	}

	private void validateImageMaxSize(MultipartFile file) {
		if (file.getSize() > MAX_FILE_SIZE) {
			throw new ValidationException(
				String.format("이미지 크기 (%s) 가 최대 사이즈 (%s) 보다 큽니다.", file.getSize(), MAX_FILE_SIZE),
				VALIDATION_IMAGE_SIZE_EXCEPTION);
		}
	}
}
