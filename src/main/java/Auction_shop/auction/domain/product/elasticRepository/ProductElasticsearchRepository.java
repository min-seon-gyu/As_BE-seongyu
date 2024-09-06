package Auction_shop.auction.domain.product.elasticRepository;

import Auction_shop.auction.domain.product.ProductDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ProductElasticsearchRepository extends ElasticsearchRepository<ProductDocument, Long> {
    Iterable<ProductDocument> findByCreatedBy(String createdBy); // 닉네임으로 제품 조회
    @Query("{\"bool\": {\"must\": [{\"match\": {\"title\": {\"query\": \"?0\", \"operator\": \"and\"}}}, {\"term\": {\"sold\": false}}]}}}")
    Iterable<ProductDocument> findByTitleLike(String title);
    List<ProductDocument> findTop20ByOrderByLikeCountDesc();
    List<ProductDocument> findTop20ByOrderByCreatedAtDesc();
}
