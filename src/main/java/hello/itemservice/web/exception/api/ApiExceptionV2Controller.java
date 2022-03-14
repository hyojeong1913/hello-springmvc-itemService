package hello.itemservice.web.exception.api;

import hello.itemservice.web.exception.exception.UserException;
import hello.itemservice.web.exception.exhandler.ErrorResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @ExceptionHandler 예외 처리 방법
 * : @ExceptionHandler 애노테이션을 선언하고, 해당 컨트롤러에서 처리하고 싶은 예외를 지정
 */
@Slf4j
@RestController
public class ApiExceptionV2Controller {

    /**
     * IllegalArgumentException 또는 그 하위 자식 클래스를 모두 처리 가능
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST) // HTTP 상태 코드 400 으로 응답
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandler(IllegalArgumentException e) {

        log.error("[exceptionHandler] ex", e);

        return new ErrorResult("BAD", e.getMessage());
    }

    /**
     * @ExceptionHandler 에 예외를 생략 가능
     * => 생략하면 메서드 파라미터의 예외가 지정된다.
     *
     * ResponseEntity 를 사용하면 HTTP 응답 코드를 프로그래밍해서 동적으로 변경 가능
     *
     * @param e
     * @return
     */
    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandler(UserException e) {

        log.error("[exceptionHandler] ex", e);

        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());

        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    /**
     * RuntimeException 은 Exception 의 자식 클래스이므로 이 메서드가 호출된다.
     * 
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // HTTP 상태 코드를 500 으로 응답
    @ExceptionHandler
    public ErrorResult exHandler(Exception e) {

        log.error("[exceptionHandler] ex", e);

        return new ErrorResult("EX", "내부 오류");
    }

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
