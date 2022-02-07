package hello.itemservice.validation;

import hello.itemservice.domain.item.Item;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class BeanValidationTest {

    /**
     * Bean Validation 테스트
     */
    @Test
    void beanValidation() {

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Item item = new Item();

        item.setItemName(" "); // 공백
        item.setPrice(0);
        item.setQuantity(10000);

        // 검증 대상을 직접 검증기에 넣고 결과 리턴
        // 결과가 비어있으면 검증 오류가 없는 것
        Set<ConstraintViolation<Item>> validations = validator.validate(item);

        for (ConstraintViolation<Item> validation : validations) {

            System.out.println("validation = " + validation);
            System.out.println("validation.getMessage() = " + validation.getMessage());
        }
    }
}
