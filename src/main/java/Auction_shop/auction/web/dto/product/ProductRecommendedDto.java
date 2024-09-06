package Auction_shop.auction.web.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRecommendedDto {

    private Long product_id;
    private String title;
    private Set<String> tradeTypes;
    private int initial_price;
    private int current_price;
    private String imageUrl;
}
