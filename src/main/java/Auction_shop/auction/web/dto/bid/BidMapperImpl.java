package Auction_shop.auction.web.dto.bid;

import Auction_shop.auction.domain.bid.Bid;
import Auction_shop.auction.domain.member.repository.MemberRepository;
import Auction_shop.auction.domain.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class BidMapperImpl implements BidMapper {

    private final MemberRepository memberRepository;

    @Override
    public BidResponseDto toResponseDto(Bid bid, String profileImageUrl, int bidCount) {
        BidResponseDto bidResponseDto = BidResponseDto.builder()
                .amount(bid.getAmount())
                .profileImageUrl(profileImageUrl)
                .bidTime(bid.getBidTime())
                .bidCount(bidCount)
                .build();
        return bidResponseDto;
    }

    @Override
    public MemberBidListResponseDto toMemberBidListResponseDto(Bid bid, Product product) {

        boolean isTopBid;
        if (product.getCurrent_price() == bid.getAmount()){
            isTopBid = true;
        } else {
            isTopBid = false;
        }

        MemberBidListResponseDto responseDto = MemberBidListResponseDto.builder()
                .imageUrl(product.getImageUrls().stream().findFirst().orElse(null))
                .initial_price(product.getInitial_price())
                .current_price(product.getCurrent_price())
                .title(product.getTitle())
                .amount(bid.getAmount())
                .bidTime(bid.getBidTime())
                .isTopBid(isTopBid)
                .build();
        return responseDto;
    }
}
