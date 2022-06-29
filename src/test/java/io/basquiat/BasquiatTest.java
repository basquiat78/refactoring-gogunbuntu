package io.basquiat;

import io.basquiat.domain.entity.FavoriteAddress;
import io.basquiat.domain.entity.Member;
import io.basquiat.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

/**
 *
 * collection value type과 @OneToMany의 경우를 테스트 해보자.
 *
 * 이와 관련 각 객체에 주석된 부분을 상황에 맞춰서 제거하거나 주석처리하면서 테스트 해보자.
 *
 */
@Transactional
@SpringBootTest
@ExtendWith(SpringExtension.class)
class BasquiatTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @Rollback(false)
    @DisplayName("STEP1: 일단 데이터 넣어보자.")
    void STEP1() {

        FavoriteAddress myHome = FavoriteAddress.builder()
                                                .city("서울")
                                                .street("자양동")
                                                .zipcode("0100")
                                                .build();
        FavoriteAddress office = FavoriteAddress.builder()
                                                .city("서울")
                                                .street("논현동")
                                                .zipcode("0101")
                                                .build();

        String memberId = "b2e26148-ff14-453c-8081-50c6243699c1";
        Member member = Member.builder()
                              .id(memberId)
                              .name("basquiat")
                              .build();
        member.getFavoriteAddresses().add(myHome);
        member.getFavoriteAddresses().add(office);

        memberRepository.save(member);
    }

    /**
     * equals와 hashcode를 재정의 한 경우와 아닌 경우를 각각 테스트해보면
     * collection value type
     *  1. 재정의 하지 않은 경우
     *     - favorite_address의 정보를 where member_id = ? 로 싹다 지운다.
     *     - 2개에서 1개가 더 추가된 3개의 row가 생성되는 것을 볼 수 있다.
     *
     *  2. 재정의 한 경우
     *     - favorite_address의 정보를 where member_id = ? 로 싹다 지운다.
     *     - 중복된 데이터를 감지해서 2개의 row가 생성되는 것을 볼 수 있다.
     *
     * @OneToMany
     *  1. 재정의 하지 않은 경우
     *      - 새로운 데이터를 인서트 한다.
     *      - OneToMany의 경우에는 인서트이후에 member_id를 favorite_address에 업데이트 한다.
     *  2. 재정의 한 경우
     *      2.1 모든 컬럼에 대한 재정의
     *          - id는 변경되기 때문에 새로운 데이터를 생성하는 insert가 날아간다.
     *      2.2 id를 제외한 컬럼에 대한 재정의
     *          - 이미 존재하는 데이터로 아무일도 일어나지 않는다.
     */
    @Test
    @Rollback(false)
    @DisplayName("STEP2: 만일 FavoriteAddress에 equals와 hashcode를 재정의 하지 않은 경우와 정의한 경우를 살펴보자.")
    void STEP2() {
        String memberId = "b2e26148-ff14-453c-8081-50c6243699c1";
        Member selected = memberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);

        // 중복된 데이터
        FavoriteAddress parentHome = FavoriteAddress.builder()
                                                    .city("서울")
                                                    .street("자양동")
                                                    .zipcode("0100")
                                                    .build();
        selected.getFavoriteAddresses().add(parentHome);
    }

}
