package Auction_shop.auction.domain.member;

import Auction_shop.auction.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String uuid;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false, length = 13)
    private String phone;

    @Column(nullable = false)
    private Long point;

    @Column(name = "profile_img")
    private String profileImage;

    @Column
    private boolean available = false;

    @Embedded
    private Address address;

    public void update(String name, String phone, String address, String detailAddress) {
        this.name = name;
        this.phone = phone;
        this.address = new Address(address, detailAddress);
    }
}
