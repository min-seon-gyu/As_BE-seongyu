package Auction_shop.auction.product.repository;

import Auction_shop.auction.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {

    //종료 시간이 지나지 않은 물건 중 경매가 종료되지 않은 물건만 서치
    @Query("SELECT a FROM Product a WHERE a.startTime <= :currentTime AND a.endTime > :currentTime AND a.isSold = false")
    List<Product> findActiveProduct(LocalDateTime currentTime);

}
