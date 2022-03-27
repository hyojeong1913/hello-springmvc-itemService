package hello.itemservice.upload.domain;

import lombok.Data;

/**
 * 업로드 파일 정보 보관
 */
@Data
public class UploadFile {

    private String UploadFileName; // 고객이 업로드한 파일명
    private String storeFileName; // 서버 내부에서 관리하는 파일명

    public UploadFile(String uploadFileName, String storeFileName) {
        UploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }
}
