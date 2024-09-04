package Auction_shop.auction.web.dto.product;

import Auction_shop.auction.domain.member.Member;
import Auction_shop.auction.domain.product.Product;
import Auction_shop.auction.domain.product.ProductDocument;

public interface ProductMapper {

    ProductResponseDto toResponseDto(Product product);
    ProductDocument toDocument(Product product);
    ProductListResponseDto toListResponeDto(ProductDocument productDocument, boolean isLiked);
    Product toEntity(ProductDto productDto, Member member);
}
