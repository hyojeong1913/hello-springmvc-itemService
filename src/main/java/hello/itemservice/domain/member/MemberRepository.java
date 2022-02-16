package hello.itemservice.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class MemberRepository {

    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;

    /**
     * 회원 가입
     *
     * @param member
     * @return
     */
    public Member save(Member member) {

        member.setId(++sequence);

        log.info("save : member = {}", member);

        store.put(member.getId(), member);

        return member;
    }

    /**
     * 회원 고유 번호로 조회
     *
     * @param id
     * @return
     */
    public Member findById(Long id) {

        return store.get(id);
    }

    /**
     * 회원 아이디로 조회
     *
     * @param longId
     * @return
     */
    public Optional<Member> findByLongId(String longId) {

//        List<Member> all = findAll();
//
//        for (Member m : all) {
//
//            if (m.getLongId().equals(longId)) {
//
//                return Optional.of(m);
//            }
//        }
//
//        return Optional.empty();

        // 위 코드를 해당 코드로 축약 가능
        return findAll().stream()
                .filter(m -> m.getLoginId().equals(longId))
                .findFirst();
    }

    /**
     * 회원 리스트 조회
     *
     * @return
     */
    public List<Member> findAll() {

        return new ArrayList<>(store.values());
    }

    /**
     * 데이터 리셋
     */
    public void clearStore() {

        store.clear();
    }
}
