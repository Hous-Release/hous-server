package hous.api.service.image.client;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import hous.common.exception.InternalServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
@Component
public class S3FileStorageClient implements FileStorageClient {

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;  // S3 버킷 이름

    private final AmazonS3 amazonS3;

    @Override
    public void uploadFile(MultipartFile file, String fileName) {
        try (InputStream inputStream = file.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, createObjectMetadata(file))
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new InternalServerException(String.format("파일 (%s) 입력 스트림을 가져오는 중 에러가 발생하였습니다", file.getOriginalFilename()));
        }
    }

    public void deleteFile(String fileName) {
        try {
            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, fileName);
            amazonS3.deleteObject(deleteObjectRequest);
        } catch (AmazonServiceException e) {
            throw new InternalServerException(String.format("파일 (%s) 을 삭제하는 중 에러가 발생하였습니다", fileName));
        } catch (SdkClientException e) {
            throw new InternalServerException(String.format("파일 (%s) 을 삭제하는 중 에러가 발생하였습니다", fileName));
        }
    }

    private ObjectMetadata createObjectMetadata(MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        return objectMetadata;
    }

    @Override
    public String getFileUrl(String fileName) {
        return amazonS3.getUrl(bucket, fileName).toString();
    }
}
