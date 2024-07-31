package Auction_shop.auction.domain.like.service;

import Auction_shop.auction.domain.like.Like;
import Auction_shop.auction.web.dto.like.LikeResponseDto;

import java.util.List;

public interface LikeService {
    Like addProductToLike(Long memberId, Long productId);
    List<Like> getLikeList(Long memberId);
    void removeProductFromLike(Long memberId, Long productId);
    int getProductLikeCount(Long productId);
}
