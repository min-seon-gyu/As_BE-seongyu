package Auction_shop.auction.domain.payments;

import Auction_shop.auction.domain.member.Member;
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
public class Payments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String paymentMethod;
    private int amount;
    private String merchantUid;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
