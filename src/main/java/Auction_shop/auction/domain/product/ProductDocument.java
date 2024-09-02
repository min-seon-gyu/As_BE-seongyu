package Auction_shop.auction.domain.product;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Set;

@Builder
@Getter
@Document(indexName = "products")
public class ProductDocument {
    @Id
    @Field(name = "product_id", type = FieldType.Long)
    private Long id;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String title;

    @Field(type = FieldType.Boolean)
    private boolean sold;  // `isSold`를 `sold`로 변경

    @Field(type = FieldType.Text)
    private String conditions;

    @Field(type = FieldType.Keyword)
    private Set<String> categories;

    @Field(type = FieldType.Keyword, name = "trade_types")
    private Set<String> tradeTypes;

    @Field(type = FieldType.Text, name = "trade_location")
    private String tradeLocation;

    @Field(type = FieldType.Integer, name = "initial_price")
    private int initialPrice;

    @Field(type = FieldType.Integer, name = "current_price")
    private int currentPrice;

    @Field(type = FieldType.Text, name = "image_url")
    private String imageUrl;

    @Field(type = FieldType.Text, name = "create_by")
    private String createdBy;

    @Field(type = FieldType.Integer, name = "like_count")
    private int likeCount;
}
