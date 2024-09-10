package Auction_shop.auction.domain.bid.controller;

import Auction_shop.auction.domain.bid.Bid;
import Auction_shop.auction.domain.bid.service.BidService;
import Auction_shop.auction.web.dto.bid.BidMapper;
import Auction_shop.auction.web.dto.bid.BidResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bids")
public class BidController {

    private final BidService bidService;
    private final BidMapper bidMapper;

    //상향식 입찰 넣기
    @PostMapping("/{productId}")
    public ResponseEntity<BidResponseDto> addBid(@PathVariable("productId") Long productId, @RequestParam int bidAmount){
        System.out.println("bidAmount = " + bidAmount);
        Bid bid = bidService.placeBid(productId, bidAmount);
        BidResponseDto collect = bidMapper.toResponseDto(bid);
        return ResponseEntity.ok(collect);
    }

    //입찰 현황 조회
    @GetMapping("/{productId}")
    public ResponseEntity<List<BidResponseDto>> getBids(@PathVariable Long productId){
        List<Bid> bids = bidService.getBidsForProduct(productId);
        List<BidResponseDto> collect = bids.stream()
                .map(bidMapper::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(collect);
    }
}
