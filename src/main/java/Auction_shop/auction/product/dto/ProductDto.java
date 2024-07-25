package Auction_shop.auction.product.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductDto {
        private String title;           // 판매글 제목
        private String product_type;    // 제품 분류
        private String trade;           // 거래 방식
        private int initial_price;       // 시작 가격

        private int minimum_price;      // 최저 가격

        private LocalDateTime startTime; // 경매 시작 시간
        private LocalDateTime endTime;   // 경매 종료 시간

        private String details;         // 설명

        @Builder
        public ProductDto(String title, String product_type, String trade, int initial_price,
                          String details, int minimum_price, LocalDateTime startTime, LocalDateTime endTime) {
                this.title = title;
                this.product_type = product_type;
                this.trade = trade;
                this.initial_price = initial_price;
                this.minimum_price = minimum_price;
                this.startTime = startTime;
                this.endTime = endTime;
                this.details = details;

        }
}
