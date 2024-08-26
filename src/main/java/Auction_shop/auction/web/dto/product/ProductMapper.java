package Auction_shop.auction.web.dto.product;

import Auction_shop.auction.domain.member.Member;
import Auction_shop.auction.domain.product.Product;

public interface ProductMapper {

    ProductResponseDto toResponseDto(Product product);
    ProductListResponseDto toListResponeDto(Product product, boolean isLiked);
    Product toEntity(ProductDto productDto, Member member);
}
