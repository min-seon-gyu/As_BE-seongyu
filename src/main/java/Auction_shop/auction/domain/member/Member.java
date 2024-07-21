package Auction_shop.auction.domain.member;

import Auction_shop.auction.domain.BaseEntity;
import Auction_shop.auction.domain.image.Image;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
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

    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Image profileImage;

    @Column
    @Builder.Default
    private boolean available = false;

    @Embedded
    private Address address;

    public void update(String name, String phone, String address, String detailAddress) {
        this.name = name;
        this.phone = phone;
        this.address = new Address(address, detailAddress);
    }

    public void setProfileImage(Image profileImage){
        this.profileImage = profileImage;
    }
}
