package hello.itemservice.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 요청 로그 인터셉터
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    public static final String LOG_ID = "logId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();

        // 요청 로그를 구분하기 위한 uuid 생성
        String uuid = UUID.randomUUID().toString();

        // 스프링 인터셉터는 호출 시점이 완전히 분리되어 있으므로
        // preHandle 에서 지정한 값을 request 등에 담아 postHandle, afterCompletion 에서 함께 사용
        request.setAttribute(LOG_ID, uuid);

        // @RequestMapping : HandlerMethod
        // 정적 리소스 : ResourceHttpRequestHandler
        if (handler instanceof HandlerMethod) {

            // 호출할 컨트롤러 메서드의 모든 정보가 포함되어 있다.
            HandlerMethod hm = (HandlerMethod) handler;
        }

        log.info("REQUEST [{}][{}][{}]", uuid, requestURI, handler);

        // 정상 호출. 다음 인터셉터나 컨트롤러 호출
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        log.info("postHandle [{}]", modelAndView);
    }

    /**
     * 예외가 발생한 경우 postHandle 가 호출되지 않기 때문에 afterCompletion 에서 종료 로그 실행
     *
     * afterCompletion 은 예외가 발생해도 호출되는 것을 보장
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        String requestURI = request.getRequestURI();
        String logId = (String) request.getAttribute(LOG_ID);

        log.info("RESPONSE [{}][{}][{}]", logId, requestURI, handler);

        if (ex != null) {

            log.error("afterCompletion error", ex);
        }
    }
}
