package hous.common.util;

import static hous.common.exception.ErrorCode.*;

import hous.common.exception.ValidationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtils {

	/**
	 * 파일의 확장자를 반환합니다.
	 * 잘못된 파일의 확장자인경우 throws ValidationException
	 *
	 * @param fileName ex) image.png
	 * @return ex) .png
	 */
	public static String getFileExtension(String fileName) {
		try {
			String extension = fileName.substring(fileName.lastIndexOf("."));
			if (extension.length() < 2) {
				throw new ValidationException(String.format("잘못된 확장자 형식의 파일 (%s) 입니다", fileName),
					FORBIDDEN_FILE_TYPE_EXCEPTION);
			}
			return extension;
		} catch (StringIndexOutOfBoundsException e) {
			throw new ValidationException(String.format("잘못된 확장자 형식의 파일 (%s) 입니다", fileName),
				FORBIDDEN_FILE_TYPE_EXCEPTION);
		}
	}
}
