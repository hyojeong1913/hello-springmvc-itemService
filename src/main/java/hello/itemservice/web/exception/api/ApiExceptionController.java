package hello.itemservice.web.exception.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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

        return new MemberDto(id, "hello " + id);
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {

        private String memberId;
        private String name;
    }
}
