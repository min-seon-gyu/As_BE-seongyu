package Auction_shop.auction.web.dto.bid;

import Auction_shop.auction.domain.bid.Bid;

public interface BidMapper {

    BidResponseDto toResponseDto(Bid bid, String profileImageUrl, int bidCount);
}
