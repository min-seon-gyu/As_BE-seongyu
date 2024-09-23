package Auction_shop.auction.web.dto.bid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberBidListResponseDto {

    private Long productId;
    private String imageUrl;
    private String title;
    private int initial_price;
    private int current_price;
    private int amount;
    private LocalDateTime bidTime;
    private boolean isTopBid;
}
