package Auction_shop.auction.domain.inquriy;

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
public class Inquiry extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inquiry_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 1000)
    private String content;

    private boolean status;

//    @ManyToOne
//    @JoinColumn(name = "member_id")
//    private Member member;

    //비즈니스 로직
    public void updateInquiry(String title, String content){
        this.title = title;
        this.content = content;
    }
}
