package hello.itemservice.domain.item;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 상품 객체
 *
 * @NotBlank 어노테이션
 *  : 빈값 + 공백만 있는 경우를 허용하지 않는다.
 *
 * @NotNull 어노테이션
 *  : null 을 허용하지 않는다.
 *
 * @Range(min = a, max = b) 어노테이션
 *  : a 에서 b 범위 안의 값이어야 한다.
 * 
 * @Max(c) 어노테이션
 *  : 최대 c 까지만 허용한다.
 *
 * @ScriptAssert 어노테이션
 *  : 오브젝트 관련 오류 처리 방법 중 하나이나 권장X
 */
@Data
//@ScriptAssert(
//        lang = "javascript",
//        script = "_this.price * _this.quantity >= 10000",
//        message = "상품의 가격 * 수량의 합은 10,000 원 이상이어야 합니다."
//)
public class Item {

    private Long id;

    @NotBlank
    private String itemName;

    @NotNull
    @Range(min = 1000, max = 1000000)
    private Integer price;

    @NotNull
    @Max(9999)
    private Integer quantity;

    private Boolean open; // 판매 여부
    private List<String> regions; // 등록 지역
    private ItemType itemType; // 상품 종류
    private String deliveryCode; // 배송 방식

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
