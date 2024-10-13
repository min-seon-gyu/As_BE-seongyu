package Auction_shop.auction.web.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPurchaseListDto {
    private Long product_id;
    private String title;
    private String productType;
    private int initial_price;
    private int current_price;
    private LocalDateTime purchase_date;
    private String imageUrl;
}
