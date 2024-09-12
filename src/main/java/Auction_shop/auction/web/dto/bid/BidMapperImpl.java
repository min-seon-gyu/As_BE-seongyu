package Auction_shop.auction.web.dto.bid;

import Auction_shop.auction.domain.bid.Bid;
import org.springframework.stereotype.Component;

@Component
public class BidMapperImpl implements BidMapper {

    @Override
    public BidResponseDto toResponseDto(Bid bid) {
        BidResponseDto bidResponseDto = BidResponseDto.builder()
                .amount(bid.getAmount())
                .memberId(bid.getMemberId())
                .bidTime(bid.getBidTime())
                .build();
        return bidResponseDto;
    }
}
