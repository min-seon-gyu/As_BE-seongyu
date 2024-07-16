package Auction_shop.auction.product.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long product_id;         // 제품ID

    //Todo 이후에 Member 엔티티 생성 시 seller 변경
    @Column(nullable = false)
    private Long seller;            // 판매자ID
    @Column(nullable = false)
    private String title;           // 판매글 제목
    @Column(nullable = false)
    private String product_type;    // 제품 분류
    @Column(nullable = false)
    private String trade;           // 거래 방식
    @Column(nullable = false)
    private int initial_price;       // 시작 가격

    private String details;         // 설명
    private String image_url;       // 제품 이미지

    @CreatedDate
    private LocalDateTime start_at; // 시작 날짜
    private LocalDateTime end_at;   // 종료 날짜

    @Builder
    public Product(Long product_id, Long seller, String title, String product_type, String trade, int initial_price,
                   String details, String image_url, LocalDateTime start_at, LocalDateTime end_at) {
        this.product_id = product_id;
        this.seller = seller;
        this.title = title;
        this.product_type = product_type;
        this.trade = trade;
        this.initial_price = initial_price;
        this.details = details;
        this.image_url = image_url;
        this.start_at = start_at;
        this.end_at = end_at;
    }
}
