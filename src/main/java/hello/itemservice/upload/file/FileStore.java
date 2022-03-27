package hello.itemservice.upload.file;

import hello.itemservice.upload.domain.UploadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 파일 저장과 관련된 업무 처리
 */
@Component
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String fileName) {

        return fileDir + fileName;
    }

    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {

        List<UploadFile> storeFileResult = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {

            if (!multipartFile.isEmpty()) {

                storeFileResult.add(storeFile(multipartFile));
            }
        }

        return storeFileResult;
    }

    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {

        if (multipartFile.isEmpty()) {

            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);

        multipartFile.transferTo(new File(getFullPath(storeFileName)));

        return new UploadFile(originalFilename, storeFileName);
    }

    /**
     * 서버 내부에서 관리하는 파일명은 유일한 이름을 생성하는 UUID 를 사용해서 충돌하지 않도록 한다.
     *
     * @param originalFilename
     * @return
     */
    private String createStoreFileName(String originalFilename) {

        String ext = extractExt(originalFilename);

        // 서버에 저장하는 파일명
        String uuid = UUID.randomUUID().toString();

        // 추출한 확장자를 서버 내부에서 관리하는 파일명에도 붙여준다.
        return uuid + "." + ext;
    }

    /**
     * 확장자를 별도로 추출
     *
     * @param originalFilename
     * @return
     */
    private String extractExt(String originalFilename) {

        int pos = originalFilename.lastIndexOf(".");

        return originalFilename.substring(pos + 1);
    }
}
