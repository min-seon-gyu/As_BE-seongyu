package Auction_shop.auction.domain.product;

import Auction_shop.auction.domain.image.Image;
import Auction_shop.auction.domain.like.Like;
import Auction_shop.auction.domain.member.Member;
import Auction_shop.auction.util.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long product_id;         // 제품ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @Column(nullable = false)
    private String title;           // 판매글 제목
    @Column(nullable = false)
    private String conditions;

    @ElementCollection
    @Column(nullable = false)
    private Set<String> categories = new HashSet<>();

    @ElementCollection
    @Column(nullable = false)
    private Set<String> tradeTypes = new HashSet<>();           // 거래 방식
    @Column(nullable = true)
    private String tradeLocation;   // 직거래 희망 거래 장소
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

    @Version
    private Long version;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    @Builder
    public Product(Long product_id, Member member, String title, String conditions, Set<String> categories, Set<String> tradeTypes, String tradeLocation, int initial_price, int minimum_price,
                   List<Image> imageList, String details, LocalDateTime startTime, LocalDateTime endTime, LocalDateTime updateTime,boolean isSold) {
        this.product_id = product_id;
        this.member = member;
        this.title = title;
        this.conditions = conditions;
        this.categories = categories;
        this.tradeTypes = tradeTypes;
        this.tradeLocation = tradeLocation;
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

    public void setMember(Member member){
        this.member = member;
    }

    public void setIsSold(boolean isSold){
        this.isSold = isSold;
    }

    public List<String> getImageUrls(){
        return imageList.stream()
                .map(Image::getAccessUrl)
                .collect(Collectors.toList());
    }

    public void updateProduct(String title, Set<String> categories,Set<String> tradeTypes,String details, String tradeLocation){
        this.title = title;
        this.categories = categories;
        this.tradeTypes = tradeTypes;
        this.details = details;
        this.tradeLocation = tradeLocation;
    }

    public void updateCurrentPrice(int current_price){
        this.current_price = current_price;
    }

    public void updateTime(LocalDateTime updateTime){
        this.updateTime = updateTime;
    }

    public long getTotalHours(){
        return Duration.between(this.startTime, this.endTime).toHours();
    }

    public void addLike(Like like) {
        likes.add(like);
        like.setProduct(this);
    }

    public void removeLike(Like like) {
        likes.remove(like);
        like.setProduct(null);
    }

    public int getLikeCount(){
        return likes.size();
    }
}
