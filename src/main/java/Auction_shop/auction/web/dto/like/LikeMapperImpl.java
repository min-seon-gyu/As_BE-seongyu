package Auction_shop.auction.web.dto.like;

import Auction_shop.auction.domain.like.Like;

public class LikeMapperImpl implements LikeMapper{

    @Override
    public LikeResponseDto toResponseDto(Like like) {
        LikeResponseDto likeResponseDto = LikeResponseDto.builder()
                .memberName(like.getMember().getName())
                .productTitle(like.getProduct().getTitle())
                .build();
        return likeResponseDto;
    }
}
