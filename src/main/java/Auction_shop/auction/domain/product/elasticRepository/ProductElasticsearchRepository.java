package Auction_shop.auction.domain.product.elasticRepository;

import Auction_shop.auction.domain.product.ProductDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Set;

public interface ProductElasticsearchRepository extends ElasticsearchRepository<ProductDocument, Long> {
    Iterable<ProductDocument> findByCreatedBy(String createdBy); // 닉네임으로 제품 조회
    @Query("{\"bool\": {\"must\": [{\"match\": {\"title\": {\"query\": \"?0\", \"operator\": \"and\"}}}, {\"term\": {\"sold\": false}}]}}}")
    Iterable<ProductDocument> findByTitleLike(String title);
    @Query("{\"bool\": {\"must\": [{\"terms\": {\"categories\": ?0}}, {\"term\": {\"sold\": false}}]}}}")
    List<ProductDocument> findByCategoriesIn(Set<String> userCategories);
    @Query("{\"bool\": {\"must\": [{\"term\": {\"sold\": false}}]}}")
    List<ProductDocument> findTop20ByOrderByLikeCountDesc();
    @Query("{\"bool\": {\"must\": [{\"term\": {\"sold\": false}}]}}")
    List<ProductDocument> findTop20ByOrderByCreatedAtDesc();
}
