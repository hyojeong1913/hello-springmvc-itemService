package hello.itemservice.web.exception.api;

import hello.itemservice.web.exception.exception.BadRequestException;
import hello.itemservice.web.exception.exception.UserException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
public class ApiExceptionController {

    /**
     * 회원 조회
     *
     * URL 에 전달된 id 의 값이 ex 또는 bad 이면 예외 발생
     *
     * @param id
     * @return
     */
    @GetMapping("/api/members/{id}")
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

    /**
     * @ResponseStatus 가 달려있는 예외
     *
     * @return
     */
    @GetMapping("/api/response-status-ex1")
    public String responseStatusEx1() {

        throw new BadRequestException();
    }

    /**
     * ResponseStatusException 예외
     *
     * 직접 변경할 수 없는 예외에 적용
     *
     * @return
     */
    @GetMapping("/api/response-status-ex2")
    public String responseStatusEx2() {

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "error.bad", new IllegalArgumentException());
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {

        private String memberId;
        private String name;
    }
}
