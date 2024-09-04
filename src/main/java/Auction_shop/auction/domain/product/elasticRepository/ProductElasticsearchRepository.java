package Auction_shop.auction.domain.product.elasticRepository;

import Auction_shop.auction.domain.product.ProductDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductElasticsearchRepository extends ElasticsearchRepository<ProductDocument, Long> {
    Iterable<ProductDocument> findByCreatedBy(String createdBy); // 닉네임으로 제품 조회
    Iterable<ProductDocument> findByTitle(String title);
    @Query("{\"bool\": {\"must\": [{\"query_string\": {\"query\": \"?0\", \"fields\": [\"title\"]}}, {\"term\": {\"sold\": false}}]}}}")
    Iterable<ProductDocument> findByTitleLike(String title);

}
