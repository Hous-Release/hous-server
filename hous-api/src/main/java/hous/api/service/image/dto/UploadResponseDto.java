package hous.api.service.image.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UploadResponseDto {
	private final String originalFileName;
	private final String uploadFileName;

	public static UploadResponseDto of(String originalFileName, String uploadFileName) {
		return new UploadResponseDto(originalFileName, uploadFileName);
	}
}
