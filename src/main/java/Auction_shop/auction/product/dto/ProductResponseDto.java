package Auction_shop.auction.product.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProductResponseDto {
    private Long product_id;         // 제품ID
    private Long seller;            // 판매자ID
    private String title;           // 판매글 제목
    private String product_type;    // 제품 분류
    private String trade;           // 거래 방식
    private int initial_price;       // 시작 가격

    private String details;         // 설명
    private List<String> imageUrls;

    private LocalDateTime start_at; // 시작 날짜
    private LocalDateTime end_at;   // 종료 날짜
}
