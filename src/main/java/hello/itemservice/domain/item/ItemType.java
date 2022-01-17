package hello.itemservice.domain.item;

/**
 * 상품 종류 ENUM
 */
public enum ItemType {

    BOOK("도서"), FOOD("음식"), ETC("기타");

    // 상품 설명
    private final String description;

    ItemType(String description) {
        this.description = description;
    }
}
