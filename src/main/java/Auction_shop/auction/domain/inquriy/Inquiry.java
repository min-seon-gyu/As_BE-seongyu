package Auction_shop.auction.domain.inquriy;

import Auction_shop.auction.domain.BaseEntity;
import Auction_shop.auction.domain.image.Image;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inquiry extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inquiry_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 1000)
    private String content;

    private boolean status;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "inquiry_id")
    private List<Image> imageList = new ArrayList<>();

//    @ManyToOne
//    @JoinColumn(name = "member_id")
//    private Member member;

    public void setImageList(List<Image> imageList){
        this.imageList = imageList;
    }

    //비즈니스 로직
    public List<String> getImageUrls(){
        return imageList.stream()
                .map(Image::getAccessUrl)
                .collect(Collectors.toList());
    }

    public void updateInquiry(String title, String content){
        this.title = title;
        this.content = content;
    }
}
