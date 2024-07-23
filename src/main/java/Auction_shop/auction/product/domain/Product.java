package Auction_shop.auction.product.domain;

import Auction_shop.auction.domain.BaseEntity;
import Auction_shop.auction.domain.image.Image;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Product{
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

    private int current_price;      // 현재 가격

    @Column(nullable = false)
    private int minimum_price;      // 최저 가격

    private LocalDateTime startTime; // 경매 시작 시간
    private LocalDateTime endTime;   // 경매 종료 시간
    private LocalDateTime updateTime;   //가격 갱신 시간

    private boolean isSold;     // 거래 성사 여부

    private String details;         // 설명

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private List<Image> imageList = new ArrayList<>();

    @Builder
    public Product(Long product_id, Long seller, String title, String product_type, String trade, int initial_price, int minimum_price,
                   List<Image> imageList, String details, LocalDateTime startTime, LocalDateTime endTime, LocalDateTime updateTime,boolean isSold) {
        this.product_id = product_id;
        this.seller = seller;
        this.title = title;
        this.product_type = product_type;
        this.trade = trade;
        this.initial_price = initial_price;
        this.imageList = imageList;
        this.current_price = initial_price;
        this.minimum_price = minimum_price;
        this.startTime = startTime;
        this.endTime = endTime;
        this.updateTime = updateTime;
        this.isSold = false;
        this.details = details;
    }

    public void setImageList(List<Image> imageList){
        this.imageList = imageList;
    }

    public List<String> getImageUrls(){
        return imageList.stream()
                .map(Image::getAccessUrl)
                .collect(Collectors.toList());
    }

    public void updateProduct(String title, String product_type, String details){
        this.title = title;
        this.product_type = product_type;
        this.details = details;
    }

    public void updateCurrentPrice(int current_price){
        this.current_price = current_price;
    }

    public void updateTime(LocalDateTime updateTime){
        this.updateTime = updateTime;
    }
}
