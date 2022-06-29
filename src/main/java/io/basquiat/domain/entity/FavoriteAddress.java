package io.basquiat.domain.entity;

import lombok.*;

import javax.persistence.*;

@Getter
//@Embeddable
//@EqualsAndHashCode
@EqualsAndHashCode(exclude = {"id"})
@Entity
@Table(name = "favorite_address")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder
    public FavoriteAddress(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

    /** 시 */
    @Column(name = "member_city")
    private String city;

    /** 동 */
    @Column(name = "member_street")
    private String street;

    /** 우편 번호 */
    @Column(name = "member_zipcode")
    private String zipcode;

    /** 전체 주소 가져오 */
    public String totalAddress() {
        return city + " " + street + ", " + zipcode;
    }

}
