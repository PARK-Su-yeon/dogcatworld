package com.techeer.abandoneddog.s3;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public String saveFile(MultipartFile multipartFile) {

        String originalFilename = multipartFile.getOriginalFilename();
        try {

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(multipartFile.getSize());
            metadata.setContentType(multipartFile.getContentType());

            amazonS3Client.putObject(new PutObjectRequest(bucket, originalFilename, multipartFile.getInputStream(), metadata));
            return amazonS3Client.getUrl(bucket, originalFilename).toString();
        } catch (IOException e) {
            // 파일 입출력 예외 처리
            e.printStackTrace();
            return "File upload failed due to an IO error: " + e.getMessage();
        } catch (AmazonS3Exception e) {
            // AWS S3 관련 예외 처리
            e.printStackTrace();
            return "File upload failed due to an Amazon S3 error: " + e.getMessage();
        } catch (SdkClientException e) {
            // AWS SDK 관련 클라이언트 예외 처리
            e.printStackTrace();
            return "File upload failed due to an SDK client error: " + e.getMessage();
        } catch (Exception e) {
            // 기타 모든 예외 처리
            e.printStackTrace();
            return "File upload failed due to an unexpected error: " + e.getMessage();
        }
    }
    @Transactional
    public String getFile(String fileName) {
        return amazonS3Client.getUrl(bucket, fileName).toString();

    }
}