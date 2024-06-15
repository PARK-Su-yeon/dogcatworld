package com.techeer.abandoneddog.s3;

import com.techeer.abandoneddog.users.dto.ResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/s3")
@RequiredArgsConstructor
@Slf4j
public class S3Controller {

    private final S3Service s3Service;


    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = s3Service.saveFile(file);
            return ResponseEntity.ok(ResultDto.res(HttpStatus.OK, "S3 업로드 성공", fileUrl));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("S3 업로드에 실패했습니다.");
        }
    }

    @GetMapping("/file")
    public ResponseEntity<?> getFile(@RequestParam("fileName") String fileName) {
        try {
            String fileUrl = s3Service.getFile(fileName);
            return ResponseEntity.ok(ResultDto.res(HttpStatus.OK, "S3 파일 조회 성공", fileUrl));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("S3 파일 조회에 실패했습니다.");
        }
    }
}
