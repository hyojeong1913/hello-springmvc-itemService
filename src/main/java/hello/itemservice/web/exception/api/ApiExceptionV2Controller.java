package hello.itemservice.web.exception.api;

import hello.itemservice.web.exception.exception.UserException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @ExceptionHandler 예외 처리 방법
 * : @ExceptionHandler 애노테이션을 선언하고, 해당 컨트롤러에서 처리하고 싶은 예외를 지정
 */
@Slf4j
@RestController
public class ApiExceptionV2Controller {

    @Data
    @AllArgsConstructor
    static class MemberDto {

        private String memberId;
        private String name;
    }

    @GetMapping("/api2/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id) {

        if (id.equals("ex")) {

            throw new RuntimeException("잘못된 사용자");
        }

        // IllegalArgumentException 발생
        if (id.equals("bad")) {

            throw new IllegalArgumentException("잘못된 입력 값");
        }

        // UserException 발생
        if (id.equals("user-ex")) {

            throw new UserException("사용자 오류");
        }

        return new MemberDto(id, "hello " + id);
    }
}
