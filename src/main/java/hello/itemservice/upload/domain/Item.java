package hello.itemservice.upload.domain;

import lombok.Data;

import java.util.List;

/**
 * 상품 도메인
 */
@Data
public class Item {

    private Long id;
    private String itemName; // 상품 이름
    private UploadFile attachFile; // 첨부파일 1개
    private List<UploadFile> imageFiles; // 이미지 파일 여러개
}
