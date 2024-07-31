package Auction_shop.auction.domain.like.controller;

import Auction_shop.auction.domain.like.Like;
import Auction_shop.auction.domain.like.service.LikeService;
import Auction_shop.auction.web.dto.like.LikeMapper;
import Auction_shop.auction.web.dto.like.LikeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/like")
public class LikeController {

    private final LikeService likeService;
    private final LikeMapper likeMapper;

    @PostMapping("/{memberId}/{productId}")
    public ResponseEntity<LikeResponseDto> addProductToLike(@PathVariable Long memberId, @PathVariable Long productId){
        Like like = likeService.addProductToLike(memberId, productId);
        LikeResponseDto collect = likeMapper.toResponseDto(like);
        return ResponseEntity.ok(collect);
    }
}
