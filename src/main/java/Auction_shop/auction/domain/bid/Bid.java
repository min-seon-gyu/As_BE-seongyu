package Auction_shop.auction.domain.bid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bid {

    private Long productId;
    private Long userId;
    private int amount;
    private LocalDateTime bidTime;

}
