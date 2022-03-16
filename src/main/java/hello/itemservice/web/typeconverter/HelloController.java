package hello.itemservice.web.typeconverter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class HelloController {

    /**
     * HTTP 요청 파라미터는 모두 문자로 처리된다.
     *
     * 따라서 요청 파라미터를 다른 타입으로 변환해서 사용하고 싶으면 타입 변환 과정을 거쳐야 한다.
     *
     * @param request
     * @return
     */
    @GetMapping("/hello-v1")
    public String helloV1(HttpServletRequest request) {

        // 문자 타입 조회
        String data = request.getParameter("data");

        // 숫자 타입으로 변경
        Integer intValue = Integer.valueOf(data);

        System.out.println("intValue = " + intValue);

        return "OK";
    }

    /**
     * 스프링 MVC 가 제공하는 @RequestParam 사용
     *
     * 스프링이 중간에서 타입을 변환
     *
     * 스프링의 타입 변환 적용 예
     * - 스프링 MVC 요청 파라미터 (@RequestParam , @ModelAttribute , @PathVariable)
     * - @Value 등으로 YML 정보 읽기
     * - XML 에 넣은 스프링 빈 정보를 변환
     * - 뷰를 렌더링 할 때
     *
     * @param data
     * @return
     */
    @GetMapping("/hello-v2")
    public String helloV2(@RequestParam Integer data) {

        System.out.println("data = " + data);

        return "OK";
    }
}
