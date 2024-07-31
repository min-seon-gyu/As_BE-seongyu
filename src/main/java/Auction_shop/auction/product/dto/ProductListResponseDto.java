package Auction_shop.auction.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductListResponseDto {

    private Long product_id;
    private String title;
    private boolean isSold;
    private String tradeLocation;
    private int initial_price;
    private String imageUrl;
}
