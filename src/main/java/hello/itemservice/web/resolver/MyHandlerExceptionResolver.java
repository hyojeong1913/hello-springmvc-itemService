package hello.itemservice.web.resolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        log.info("call resolver", ex);

        try {

            // IllegalArgumentException 이 발생하면
            // response.sendError(400) 를 호출해서 HTTP 상태 코드를 400 으로 지정하고,
            // 빈 ModelAndView 를 반환
            if (ex instanceof IllegalArgumentException) {

                log.info("IllegalArgumentException resolver to 400");

                // 예외를 response.sendError(xxx) 호출로 변경해서 서블릿에서 상태 코드에 따른 오류를 처리하도록 위임
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());

                return new ModelAndView();
            }
        }
        catch (IOException e) {
            log.error("resolver ex", e);
        }

        // null 을 반환하면,
        // 다음 ExceptionResolver 를 찾아서 실행.
        // 만약 처리할 수 있는 ExceptionResolver 가 없으면 예외 처리가 안되고,
        // 기존에 발생한 예외를 서블릿 밖으로 던진다.
        return null;
    }
}
