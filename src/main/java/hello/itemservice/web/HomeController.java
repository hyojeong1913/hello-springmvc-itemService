package hello.itemservice.web;

import hello.itemservice.domain.member.Member;
import hello.itemservice.domain.member.MemberRepository;
import hello.itemservice.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

//    @GetMapping("/")
    public String home() {

        return "home";
    }

//    @GetMapping("/")
    public String homeLogin(
            @CookieValue(name = "memberId", required = false) Long memberId,
            Model model
    ) {

        if (memberId == null) {

            return "home";
        }

        Member loginMember = memberRepository.findById(memberId);

        if (loginMember == null) {

            return "home";
        }

        model.addAttribute("member", loginMember);

        return "loginHome";
    }

//    @GetMapping("/")
    public String homeLoginV2(
            HttpServletRequest request,
            Model model
    ) {
        
        // 세션 관리자에 저장된 회원 정보 조회
        Member member = (Member) sessionManager.getSessionV2(request);

        // 로그인
        if (member == null) {

            return "home";
        }

        model.addAttribute("member", member);

        return "loginHome";
    }

//    @GetMapping("/")
    public String homeLoginV3(
            HttpServletRequest request,
            Model model
    ) {

        HttpSession session = request.getSession(false);

        // 세션이 없는 경우 로그인 홈으로 이동
        if (session == null) {

            return "home";
        }

        // 로그인 시점에 세션에 보관한 회원 객체 조회
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        // 세션에 회원 데이터가 없으면 로그인 홈으로 이동
        if (loginMember == null) {

            return "home";
        }

        model.addAttribute("member", loginMember);

        return "loginHome";
    }

    /**
     * 스프링은 세션을 더 편리하게 사용할 수 있도록 @SessionAttribute 을 지원
     *
     * @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember
     * : 이미 로그인된 사용자를 찾을 때 사용, 이 기능은 세션을 생성하지 않는다.
     *
     * @param loginMember
     * @param model
     * @return
     */
    @GetMapping("/")
    public String homeLoginV3Spring(
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
            Model model
    ) {

        // 세션에 회원 데이터가 없으면 로그인 홈으로 이동
        if (loginMember == null) {

            return "home";
        }

        model.addAttribute("member", loginMember);

        return "loginHome";
    }
}
