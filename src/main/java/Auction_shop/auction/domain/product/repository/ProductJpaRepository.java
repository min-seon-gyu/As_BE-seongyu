package Auction_shop.auction.domain.product.repository;

import Auction_shop.auction.domain.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductJpaRepository extends JpaRepository<Product,Long> {

    //종료 시간이 지나지 않은 물건 중 경매가 종료되지 않은 물건만 서치
    @Query("SELECT p FROM Product p WHERE p.startTime <= :currentTime AND p.endTime > :currentTime AND p.isSold = false")
    Page<Product> findActiveProduct(@Param("currentTime") LocalDateTime currentTime, Pageable pageable);

    @Query("SELECT p.current_price FROM Product p WHERE p.product_id = :productId")
    int findCurrentPriceById(@Param("productId") Long productId);

    List<Product> findAllByMemberId(Long memberId);
}
