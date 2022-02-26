package hello.itemservice.web.filter;

import hello.itemservice.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 인증 체크 필터
 */
@Slf4j
public class LoginCheckFilter implements Filter {

    // 인증과 무관하게 항상 허용
    // 화이트 리스트를 제외한 나머지 모든 경로에는 인증 체크 로직을 적용
    private static final String[] whilteList = {"/", "/members/add", "/login", "/logout", "/css/*"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {

            log.info("인증 체크 필터 시작 {}", requestURI);

            // 화이트 리스트를 제외한 모든 경우에 인증 체크 로직을 적용
            if (isLoginCheckPath(requestURI)) {

                log.info("인증 체크 로직 실행 {}", requestURI);

                HttpSession session = httpRequest.getSession(false);

                if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {

                    log.info("미인증 사용자 요청 {}", requestURI);

                    // 로그인으로 redirect
                    httpResponse.sendRedirect("/login?redirectURL=" + requestURI);

                    // redirect 가 응답으로 적용되고 요청이 끝난다.
                    return;
                }
            }

            chain.doFilter(request, response);

        } catch (Exception e) {

            throw e;

        } finally {

            log.info("인증 체크 필터 종료 {}", requestURI);
        }
    }

    /**
     * 화이트 리스트에 포함된 경로인지 체크
     *
     * @param requestURI
     * @return
     */
    private boolean isLoginCheckPath(String requestURI) {

        return !PatternMatchUtils.simpleMatch(whilteList, requestURI);
    }
}
