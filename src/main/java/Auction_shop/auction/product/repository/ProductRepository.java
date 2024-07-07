package Auction_shop.auction.product.repository;

import Auction_shop.auction.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {

}
