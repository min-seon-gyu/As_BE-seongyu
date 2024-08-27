package Auction_shop.auction.domain.member;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private String address;
    private String detailAddress;
    private String zipcode;
}
