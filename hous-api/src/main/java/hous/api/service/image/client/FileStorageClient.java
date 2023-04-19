package hous.api.service.image.client;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageClient {

    void uploadFile(MultipartFile file, String fileName);

    String getFileUrl(String fileName);
}
