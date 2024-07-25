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
    private String title;           // 판매글 제목
    private String product_type;    // 제품 분류
    private String trade;           // 거래 방식
    private int initial_price;       // 시작 가격
    private int minimum_price;      // 최저 가격
    private boolean isSold;         // 판매 여부

    private LocalDateTime startTime; // 경매 시작 시간
    private LocalDateTime endTime;   // 경매 종료 시간

    private String details;         // 설명
    private List<String> imageUrls;     //이미지 url


}
