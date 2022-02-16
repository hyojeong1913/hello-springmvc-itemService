package hello.itemservice.web.member;

import hello.itemservice.domain.member.Member;
import hello.itemservice.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberRepository memberRepository;

    /**
     * 회원 가입 페이지로 이동
     *
     * @param member
     * @return
     */
    @GetMapping("/add")
    public String addForm(@ModelAttribute("member") Member member) {

        return "members/addMemberForm";
    }

    /**
     * 회원 가입
     *
     * @param member
     * @param bindingResult
     * @return
     */
    @PostMapping("/add")
    public String save(@Valid @ModelAttribute Member member, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {

            return "members/addMemberForm";
        }

        memberRepository.save(member);

        return "redirect:/";
    }
}
