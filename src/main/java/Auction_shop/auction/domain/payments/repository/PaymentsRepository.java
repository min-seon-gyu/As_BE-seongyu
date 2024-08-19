package Auction_shop.auction.domain.payments.repository;

import Auction_shop.auction.domain.payments.Payments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentsRepository extends JpaRepository<Payments, Long> {
}
