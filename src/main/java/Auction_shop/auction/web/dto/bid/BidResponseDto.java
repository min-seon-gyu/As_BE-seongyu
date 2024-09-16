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
public class BidResponseDto {
    private int bidCount;
    private int amount;
    private LocalDateTime bidTime;
    private String profileImageUrl;
}
