package Auction_shop.auction.domain.product.elasticRepository;

import Auction_shop.auction.domain.product.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductElasticsearchRepository extends ElasticsearchRepository<ProductDocument, Long> {
    Iterable<ProductDocument> findByMemberId(Long memberId); // 회원 ID로 제품 조회
}
