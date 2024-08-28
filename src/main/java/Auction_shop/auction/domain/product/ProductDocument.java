package Auction_shop.auction.domain.product;

import jakarta.persistence.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Set;

@Document(indexName = "products")
public class ProductDocument {
    @Id
    private Long id;
    private String title;
    private boolean isSold;
    private String conditions;
    private Set<String> categories;
    private Set<String> tradeTypes;
    private String tradeLocation;
    private int initial_price;
    private int current_price;
    private String imageUrl;
    private String createdBy;
    private int likeCount;
    private boolean isLiked;
}
