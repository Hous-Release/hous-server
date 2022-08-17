package hous.server.service.image.provider.dto.request;

import hous.server.common.type.FileType;
import lombok.*;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageUploadFileRequest implements UploadFileRequest {

    private static final String SEPARATOR = "/";
    private static final String IMAGE_CONTENT_TYPE_TYPE = "image";

    private FileType type;

    public static ImageUploadFileRequest of(FileType type) {
        return new ImageUploadFileRequest(type);
    }

    public String getFileNameWithBucketDirectory(String originalFileName) {
        return type.createUniqueFileNameWithExtension(originalFileName);
    }
}
