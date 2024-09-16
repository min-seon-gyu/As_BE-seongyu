package Auction_shop.auction.web.dto.bid;

import Auction_shop.auction.domain.bid.Bid;
import Auction_shop.auction.domain.member.Member;
import Auction_shop.auction.domain.member.repository.MemberRepository;
import Auction_shop.auction.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

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
}
