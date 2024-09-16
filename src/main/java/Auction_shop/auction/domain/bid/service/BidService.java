package Auction_shop.auction.domain.bid.service;

import Auction_shop.auction.domain.bid.Bid;
import Auction_shop.auction.domain.bid.repository.BidJpaRepository;
import Auction_shop.auction.domain.bid.repository.BidRedisRepository;
import Auction_shop.auction.domain.member.Member;
import Auction_shop.auction.domain.member.repository.MemberRepository;
import Auction_shop.auction.web.dto.bid.BidMapper;
import Auction_shop.auction.web.dto.bid.BidResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BidService {
    private final BidRedisRepository bidRedisRepository;
    private final BidJpaRepository bidJpaRepository;
    private final MemberRepository memberRepository;
    private final BidMapper bidMapper;

    public List<BidResponseDto> getBidsForProduct(Long productId){
        List<Bid> bids = bidRedisRepository.findBidsByProductId(productId);

        if (bids.isEmpty()){
            bids = bidJpaRepository.findByProductId(productId);
            for(Bid bid : bids){
                System.out.println("bid.getAmount() = " + bid.getAmount());
                bidRedisRepository.save(bid);
            }
        }

        List<Long> memberIds = bids.stream()
                .map(Bid::getMemberId)
                .distinct()
                .collect(Collectors.toList());

        Map<Long, Member> memberMap = memberRepository.findAllById(memberIds)
                .stream()
                .collect(Collectors.toMap(Member::getId, member -> member));

        List<BidResponseDto> bidResponseDtos = new ArrayList<>();

        for (int i = 0; i < bids.size(); i++) {
            Bid bid = bids.get(i);
            Member member = memberMap.get(bid.getMemberId());
            String profileImageUrl = (member != null && member.getProfileImage() != null)
                    ? member.getProfileImage().getAccessUrl()
                    : null;

            // BidMapper를 사용하여 BidResponseDto 생성
            BidResponseDto dto = bidMapper.toResponseDto(bid, profileImageUrl, i + 1); // bidCount는 i + 1
            bidResponseDtos.add(dto);
        }

        Collections.reverse(bidResponseDtos);

        return bidResponseDtos;
    }

    public Bid getHighestBidForProduct(Long productId){
        Bid highestBid = bidRedisRepository.findHighestBidByProductId(productId);

        if (highestBid == null){
            highestBid = bidJpaRepository.findHighestBidByProductId(productId);
        }

        return highestBid;
    }
}
