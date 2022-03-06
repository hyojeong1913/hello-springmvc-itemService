package hello.itemservice.web.exception.servlet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 서블릿 예외 컨트롤러
 */
@Slf4j
@Controller
public class ServletExController {

    /**
     * Exception 의 경우 서버 내부에서 처리할 수 없는 오류가 발생한 것으로 간주해서 HTTP 상태 코드 500 을 반환
     */
    @GetMapping("/error-ex")
    public void errorEx() {

        throw new RuntimeException("예외 발생");
    }

    /**
     * 서블릿 컨테이너에게 오류가 발생했다는 점을 전달 가능
     *
     * sendError 흐름
     * : WAS ( sendError 호출 기록 확인 ) <- 필터 <- 서블릿 <- 인터셉터 <- 컨트롤러
     *
     * response.sendError() 를 호출하면 response 내부에는 오류가 발생했다는 상태를 저장
     * 서블릿 컨테이너는 응답 전에 response 에 sendError() 가 호출되었는지 확인
     * 호출되었다면 설정한 오류 코드에 맞추어 기본 오류 페이지를 보여준다.
     *
     * response.sendError(HTTP 상태 코드, 오류 메시지)
     *
     * @param response
     * @throws IOException
     */
    @GetMapping("/error-404")
    public void error404(HttpServletResponse response) throws IOException {

        response.sendError(404, "404 오류");
    }

    /**
     * response.sendError(HTTP 상태 코드)
     *
     * @param response
     * @throws IOException
     */
    @GetMapping("/error-500")
    public void error500(HttpServletResponse response) throws IOException {

        response.sendError(500);
    }

    @GetMapping("/error-400")
    public void error400(HttpServletResponse response) throws IOException {

        response.sendError(400, "400 오류");
    }
}
