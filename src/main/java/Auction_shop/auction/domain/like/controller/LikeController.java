package Auction_shop.auction.domain.like.controller;

import Auction_shop.auction.domain.like.Like;
import Auction_shop.auction.domain.like.service.LikeService;
import Auction_shop.auction.web.dto.like.LikeListResponseDto;
import Auction_shop.auction.web.dto.like.LikeMapper;
import Auction_shop.auction.web.dto.like.LikeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/{memberId}")
    public ResponseEntity<List<LikeListResponseDto>> getLikeList(@PathVariable Long memberId){
        List<Like> likes = likeService.getLikeList(memberId);

        if (likes.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        List<LikeListResponseDto> collect = likes.stream()
                .map(likeMapper::toListResponseDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(collect);
    }

    @DeleteMapping("/{memberId}/{productId}")
    public void removeProductFromLike(@PathVariable Long memberId, @PathVariable Long productId){
        likeService.removeProductFromLike(memberId, productId);
    }
}
