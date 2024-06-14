package com.techeer.abandoneddog.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3Client amazonS3Client3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public String saveFile(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3Client3.putObject(bucket, originalFilename, multipartFile.getInputStream(), metadata);
        return amazonS3Client3.getUrl(bucket, originalFilename).toString();
    }

    @Transactional
    public String getFile(String fileName) {
        return amazonS3Client3.getUrl(bucket, fileName).toString();
    }
}