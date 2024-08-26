package Auction_shop.auction.web.dto.product;

import Auction_shop.auction.domain.member.Member;
import Auction_shop.auction.domain.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductMapplerImpl implements ProductMapper{

    @Override
    public ProductResponseDto toResponseDto(Product product) {
        ProductResponseDto responseDto = ProductResponseDto.builder()
                .memberId(product.getMember().getId())
                .product_id(product.getProduct_id())
                .title(product.getTitle())
                .conditions(product.getConditions())
                .categories(product.getCategories())
                .tradeTypes(product.getTradeTypes())
                .tradeLocation(product.getTradeLocation())
                .initial_price(product.getInitial_price())
                .current_price(product.getCurrent_price())
                .minimum_price(product.getMinimum_price())
                .createdBy(product.getCreatedBy())
                .startTime(product.getStartTime())
                .likeCount(product.getLikeCount())
                .endTime(product.getEndTime())
                .details(product.getDetails())
                .isSold(product.isSold())
                .imageUrls(product.getImageUrls())
                .build();

        return responseDto;
    }

    @Override
    public ProductListResponseDto toListResponeDto(Product product, boolean isLiked) {
        String imageUrl = null;
        if (!product.getImageList().isEmpty()) {
            imageUrl = product.getImageList().get(0).getAccessUrl(); // 첫 번째 이미지 URL 가져오기
        }

        ProductListResponseDto responseDto = ProductListResponseDto.builder()
                .product_id(product.getProduct_id())
                .title(product.getTitle())
                .conditions(product.getConditions())
                .initial_price(product.getInitial_price())
                .categories(product.getCategories())
                .tradeTypes(product.getTradeTypes())
                .current_price(product.getCurrent_price())
                .tradeLocation(product.getTradeLocation())
                .createdBy(product.getCreatedBy())
                .likeCount(product.getLikeCount())
                .isSold(product.isSold())
                .imageUrl(imageUrl)
                .isLiked(isLiked)
                .build();
        return responseDto;
    }

    @Override
    public Product toEntity(ProductDto productDto, Member member) {
        Product product = Product.builder()
                .title(productDto.getTitle())
                .member(member)
                .conditions(productDto.getConditions())
                .categories(productDto.getCategories())
                .tradeTypes(productDto.getTradeTypes())
                .tradeLocation(productDto.getTradeLocation())
                .initial_price(productDto.getInitial_price())
                .startTime(productDto.getStartTime())
                .endTime(productDto.getEndTime())
                .updateTime(productDto.getStartTime())
                .isSold(false)
                .minimum_price(productDto.getMinimum_price())
                .details(productDto.getDetails())
                .build();
        return product;
    }
}
