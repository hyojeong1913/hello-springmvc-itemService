package hello.itemservice.web.exception.exhandler.advice;

import hello.itemservice.web.exception.exception.UserException;
import hello.itemservice.web.exception.exhandler.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @ControllerAdvice 또는 @RestControllerAdvice 를 사용하면
 * 정상 코드와 예외 처리 코드를 분리 가능
 *
 * @ControllerAdvice
 *  : 대상으로 지정한 여러 컨트롤러에 @ExceptionHandler , @InitBinder 기능을 부여해주는 역할
 *  : 대상을 지정하지 않으면 모든 컨트롤러에 적용
 *
 * @RestControllerAdvice = @ControllerAdvice + @ResponseBody
 * ( @Controller, @RestController 의 유사 관계 )
 *
 * 특정 애노테이션이 있는 컨트롤러를 지정 가능
 * 특정 패키지를 직접 지정 가능하며 해당 패키지와 그 하위에 있는 컨트롤러가 대상이 된다.
 * 특정 클래스를 지정 가능
 */
@Slf4j
@RestControllerAdvice(basePackages = "hello.itemservice.web.exception.api")
public class ExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST) // HTTP 상태 코드 400 으로 응답
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandler(IllegalArgumentException e) {

        log.error("[exceptionHandler] ex", e);

        return new ErrorResult("BAD", e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandler(UserException e) {

        log.error("[exceptionHandler] ex", e);

        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());

        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // HTTP 상태 코드를 500 으로 응답
    @ExceptionHandler
    public ErrorResult exHandler(Exception e) {

        log.error("[exceptionHandler] ex", e);

        return new ErrorResult("EX", "내부 오류");
    }
}
