package hello.itemservice.web.login;

import hello.itemservice.domain.login.LoginService;
import hello.itemservice.domain.member.Member;
import hello.itemservice.web.SessionConst;
import hello.itemservice.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final SessionManager sessionManager;

    /**
     * 로그인 페이지로 이동
     *
     * @param form
     * @return
     */
    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {

        return "login/loginForm";
    }

    /**
     * 로그인
     *
     * @param form
     * @param bindingResult
     * @param response
     * @return
     */
//    @PostMapping("/login")
    public String login(
            @Valid @ModelAttribute LoginForm form,
            BindingResult bindingResult,
            HttpServletResponse response
    ) {

        if (bindingResult.hasErrors()) {

            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if (loginMember == null) {

            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");

            return "login/loginForm";
        }

        /**
         * 로그인 성공 처리
         */

        // 쿠키에 시간 정보를 주지 않으면 세션 쿠키 (브라우저 종료 시 모두 종료)
        Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
        response.addCookie(idCookie);

        return "redirect:/";
    }

    /**
     * 로그인
     *
     * @param form
     * @param bindingResult
     * @param response
     * @return
     */
//    @PostMapping("/login")
    public String loginV2(
            @Valid @ModelAttribute LoginForm form,
            BindingResult bindingResult,
            HttpServletResponse response
    ) {

        if (bindingResult.hasErrors()) {

            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if (loginMember == null) {

            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");

            return "login/loginForm";
        }

        /**
         * 로그인 성공 처리
         */

        // 세션 관리자를 통해 세션을 생성하고, 회원 데이터 보관
        sessionManager.createSession(loginMember, response);

        return "redirect:/";
    }

    /**
     * 로그인
     *
     * @param form
     * @param bindingResult
     * @param request
     * @return
     */
    @PostMapping("/login")
    public String loginV3(
            @Valid @ModelAttribute LoginForm form,
            BindingResult bindingResult,
            HttpServletRequest request
    ) {

        if (bindingResult.hasErrors()) {

            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if (loginMember == null) {

            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");

            return "login/loginForm";
        }

        /**
         * 로그인 성공 처리
         */

        // 세션이 있으면 있는 세션 반환, 없으면 신규 세션 생성
        HttpSession session = request.getSession();

        // 세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "redirect:/";
    }

    /**
     * 로그아웃
     *
     * @param response
     * @return
     */
//    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {

        expireCookie(response, "memberId");

        return "redirect:/";
    }

    /**
     * 로그아웃
     *
     * @param request
     * @return
     */
//    @PostMapping("/logout")
    public String logoutV2(HttpServletRequest request) {

        sessionManager.expireCookie(request);

        return "redirect:/";
    }

    /**
     * 로그아웃
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public String logoutV3(HttpServletRequest request) {

        // 세션이 있으면 기존 세션을 반환, 없으면 null 반환
        HttpSession session = request.getSession(false);

        // 세션 존재하는 경우 세션 삭제
        if (session != null) {

            session.invalidate();
        }

        return "redirect:/";
    }

    private void expireCookie(HttpServletResponse response, String cookieName) {

        Cookie cookie = new Cookie(cookieName, null);

        // 서버에서 해당 쿠키의 종료 날짜를 0으로 지정
        cookie.setMaxAge(0);

        response.addCookie(cookie);
    }
}
