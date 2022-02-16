package hello.itemservice.domain.login;

import hello.itemservice.domain.member.Member;
import hello.itemservice.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    /**
     * 로그인
     *
     * @param loginId
     * @param password
     * @return 실패인 경우 null
     */
    public Member login(String loginId, String password) {

        ////////// -- 첫 번째 방법 -- //////////

//        Optional<Member> findMemberOptional = memberRepository.findByLoginId(loginId);
//
//        Member member = findMemberOptional.get();
//
//        if (member.getPassword().equals(password)) {
//
//            return member;
//        }
//        else {
//
//            return null;
//        }

        ////////// -- 두 번째 방법 -- //////////

//        Optional<Member> byLoginId = memberRepository.findByLoginId(loginId);
//
//        return byLoginId.filter(m -> m.getPassword().equals(password))
//                .orElse(null);

        ////////// -- 세 번째 방법 -- //////////

        return memberRepository.findByLoginId(loginId)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);
    }
}
