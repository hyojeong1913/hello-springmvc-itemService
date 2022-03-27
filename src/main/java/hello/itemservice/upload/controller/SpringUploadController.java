package hello.itemservice.upload.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

/**
 * 스프링은 MultipartFile 이라는 인터페이스로 multipart 파일을 매우 편리하게 지원
 */
@Slf4j
@Controller
@RequestMapping("/spring")
public class SpringUploadController {

    @Value("${file.dir}")
    private String fileDir;

    @GetMapping("/upload")
    public String newFile() {

        return "upload/form";
    }

    @PostMapping("/upload")
    public String saveFile(
            @RequestParam String itemName,
            @RequestParam MultipartFile file,
            HttpServletRequest request
    ) throws IOException {

        log.info("request = {}", request);
        log.info("itemName = {}", itemName);
        log.info("multipartFile = {}", file);

        if (!file.isEmpty()) {

            // file.getOriginalFilename() : 업로드 파일명을 출력하는 multipart 주요 메서드
            String fullPath = fileDir + file.getOriginalFilename();

            log.info("파일 저장 fullPath = {}", fullPath);

            // file.transferTo() : 파일을 저장하는 multipart 주요 메서드
            file.transferTo(new File(fullPath));
        }

        return "upload/form";
    }
}
