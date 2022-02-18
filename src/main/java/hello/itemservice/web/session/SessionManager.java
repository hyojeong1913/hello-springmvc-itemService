package hello.itemservice.web.session;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {

    public static final String SESSION_COOKIE_NAME = "mySessionId";
    private Map<String, Object> sessionStore = new ConcurrentHashMap<>();

    /**
     * 세션 생성
     *
     * sessionId (임의의 추정 불가능한 랜덤 값) 생성
     * 세션 저장소에 sessionId 와 보관할 값 저장
     * sessionId 로 응답 쿠키를 생성해서 클라이언트에 전달
     *
     * @param value
     * @param response
     */
    public void createSession(Object value, HttpServletResponse response) {

        // sessionId 생성
        String sessionId = UUID.randomUUID().toString();

        // 값을 세션에 저장
        sessionStore.put(sessionId, value);

        // 쿠키 생성
        Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
        response.addCookie(mySessionCookie);
    }

    /**
     * 세션 조회 방법1
     *
     * @param request
     * @return
     */
    public Object getSessionV1(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();

        if (cookies == null) {

            return null;
        }

        for (Cookie cookie : cookies) {

            if (cookie.getName().equals(SESSION_COOKIE_NAME)) {

                return sessionStore.get(cookie.getValue());
            }
        }

        return null;
    }

    /**
     * 세션 조회 방법2
     *
     * 아래에 만든 findCookie() 함수 사용
     *
     * @param request
     * @return
     */
    public Object getSessionV2(HttpServletRequest request) {

        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);

        if (sessionCookie == null) {

            return null;
        }

        return sessionStore.get(sessionCookie.getValue());
    }

    /**
     * 쿠키 조회
     *
     * @param request
     * @param cookieName
     * @return
     */
    public Cookie findCookie(HttpServletRequest request, String cookieName) {

        Cookie[] cookies = request.getCookies();

        if (cookies == null) {

            return null;
        }

        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findAny()
                .orElse(null);
    }

    /**
     * 세션 만료
     *
     * @param request
     */
    public void expireCookie(HttpServletRequest request) {

        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);

        if (sessionCookie != null) {

            sessionStore.remove(sessionCookie.getValue());
        }
    }
}
