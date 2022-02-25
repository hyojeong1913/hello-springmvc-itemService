package hello.itemservice.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

/**
 * 모든 요청을 로그로 남기는 필터
 */
@Slf4j
public class LogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        log.info("log filter init");
    }

    /**
     * HTTP 요청이 오면 doFilter 호출
     *
     * @param request HTTP 요청이 아닌 경우까지 고려해서 만든 인터페이스
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        log.info("log filter doFilter");

        // HttpServletRequest 로 다운캐스팅
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String requestURI = httpRequest.getRequestURI();

        // HTTP 요청을 구분하기 위해 요청당 임의의 uuid 생성
        String uuid = UUID.randomUUID().toString();

        try {

            log.info("REQUEST [{}][{}]", uuid, requestURI);

            // 다음 필터가 있으면 필터를 호출하고, 필터가 없으면 서블릿을 호출
            chain.doFilter(request, response);

        } catch (Exception e) {

            throw e;

        } finally {

            log.info("RESPONSE [{}][{}]", uuid, requestURI);
        }
    }

    @Override
    public void destroy() {

        log.info("log filter destroy");
    }
}
