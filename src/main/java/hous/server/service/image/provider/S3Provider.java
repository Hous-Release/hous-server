package hous.server.service.image.provider;

import hous.server.service.image.client.S3FileStorageClient;
import hous.server.service.image.provider.dto.request.UploadFileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Component
public class S3Provider {

    private final S3FileStorageClient fileStorageClient;

    public String uploadFile(UploadFileRequest request, MultipartFile file) {
        request.validateAvailableContentType(file.getContentType());
        String fileName = request.getFileNameWithBucketDirectory(file.getOriginalFilename());
        fileStorageClient.uploadFile(file, fileName);
        return fileStorageClient.getFileUrl(fileName);
    }

    public void deleteFile(String fileName) {
        if (fileName != null) {
            String[] image = fileName.split(".com/");
            fileStorageClient.deleteFile(image[1]);
        }
    }
}
