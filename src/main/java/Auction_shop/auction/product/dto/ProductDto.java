package Auction_shop.auction.product.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductDto {
        private Long seller;            // 판매자ID
        private String title;           // 판매글 제목
        private String product_type;    // 제품 분류
        private String trade;           // 거래 방식
        private int initial_price;       // 시작 가격

        private String details;         // 설명

        private LocalDateTime start_at; // 시작 날짜
        private LocalDateTime end_at;   // 종료 날짜

        @Builder
        public ProductDto(Long seller, String title, String product_type, String trade, int initial_price,
                          String details, String image_url, LocalDateTime start_at, LocalDateTime end_at) {
                this.seller = seller;
                this.title = title;
                this.product_type = product_type;
                this.trade = trade;
                this.initial_price = initial_price;
                this.start_at = start_at;
                this.end_at = end_at;
        }
}
