package Auction_shop.auction.web.dto.like;

import Auction_shop.auction.domain.like.Like;
import Auction_shop.auction.domain.product.Product;
import org.springframework.stereotype.Component;

@Component
public class LikeMapperImpl implements LikeMapper{

    @Override
    public LikeResponseDto toResponseDto(Like like) {
        LikeResponseDto likeResponseDto = LikeResponseDto.builder()
                .memberName(like.getMember().getName())
                .productTitle(like.getProduct().getTitle())
                .build();
        return likeResponseDto;
    }

    @Override
    public LikeListResponseDto toListResponseDto(Like like) {

        Product product = like.getProduct();
        String imageUrl = null;

        if(!product.getImageList().isEmpty()){
            imageUrl = product.getImageUrls().get(0);
        }

        LikeListResponseDto responseDto = LikeListResponseDto.builder()
                .product_id(product.getProduct_id())
                .title(product.getTitle())
                .initial_price(product.getInitial_price())
                .imageUrl(imageUrl)
                .isSold(product.isSold())
                .tradeLocation(product.getTradeLocation())
                .build();

        return responseDto;
    }
}
