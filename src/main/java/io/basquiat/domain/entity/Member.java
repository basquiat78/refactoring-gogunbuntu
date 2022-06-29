package io.basquiat.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@Table(name = "member")
@ToString(exclude = {"favoriteAddresses"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Builder
    public Member(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /** 사용자 아이디 */
    @Id
    private String id;

    /** 사용자 이름 */
    private String name;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "member_id")
    //@ElementCollection
    //@CollectionTable(name = "favorite_address", joinColumns = @JoinColumn(name = "member_id"))
    private Set<FavoriteAddress> favoriteAddresses = new HashSet<>();

}
