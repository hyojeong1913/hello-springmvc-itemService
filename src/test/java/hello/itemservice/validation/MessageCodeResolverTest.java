package hello.itemservice.validation;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * MessageCodesResolver
 *  - 검증 오류 코드로 메시지 코드들을 생성
 *  - MessageCodesResolver 인터페이스이고 DefaultMessageCodesResolver 는 기본 구현체
 *  - 주로 ObjectError, FieldError 와 함께 사용
 *
 * 동작 방식
 *  - rejectValue() , reject() 는 내부에서 MessageCodesResolver 를 사용하며 메시지 코드들을 생성
 *  - 오류 코드를 하나가 아니라 여러 오류 코드를 가질 수 있고, MessageCodesResolver 를 통해서 생성된 순서대로 오류 코드를 보관
 *
 * 오류 메시지 출력
 *  - 타임리프 화면을 렌더링 할 때 th:errors 가 실행
 *  - 만약 이때 오류가 있다면 생성된 오류 메시지 코드를 순서대로 돌아가면서 메시지를 찾는데, 없으면 디폴트 메시지를 출력
 */
public class MessageCodeResolverTest {

    MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();

    /**
     * 객체 오류
     *
     * 객체 오류의 경우 다음 순서로 2가지 생성
     *  1. code + "." + object name
     *  2. code
     */
    @Test
    void messageCodesResolverObject() {

        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item");

        for (String messageCode : messageCodes) {

            System.out.println("messageCode = " + messageCode);
        }

        assertThat(messageCodes).containsExactly("required.item", "required");
    }

    /**
     * 필드 오류
     *
     * 필드 오류의 경우 다음 순서로4가지 메시지 코드 생성
     *
     *  1. code + "." + object name + "." + field
     *  2. code + "." + field
     *  3. code + "." + field type
     *  4. code
     */
    @Test
    void messageCodesResolverField() {

        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item", "itemName", String.class);

        for (String messageCode : messageCodes) {

            System.out.println("messageCode = " + messageCode);
        }

        assertThat(messageCodes).containsExactly(
                "required.item.itemName",
                "required.itemName",
                "required.java.lang.String",
                "required"
        );
    }
}
