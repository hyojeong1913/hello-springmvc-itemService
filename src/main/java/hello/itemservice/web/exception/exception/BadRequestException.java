package hello.itemservice.web.exception.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *  @ResponseStatus 애노테이션을 적용하여 HTTP 상태 코드를 변경
 *
 * BadRequestException 예외가 컨트롤러 밖으로 넘어가면
 * ResponseStatusExceptionResolver 예외가 해당 애노테이션을 확인해서
 * 오류 코드를 HttpStatus.BAD_REQUEST (400) 으로 변경하고, 메시지도 담는다.
 *
 * reason 을 MessageSource 에서 찾는 기능도 제공
 */
//@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "잘못된 요청 오류")
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "error.bad")
public class BadRequestException extends RuntimeException {
}
