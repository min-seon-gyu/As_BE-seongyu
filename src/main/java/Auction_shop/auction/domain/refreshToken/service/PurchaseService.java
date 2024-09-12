package Auction_shop.auction.domain.refreshToken.service;

import Auction_shop.auction.domain.purchase.Purchase;
import Auction_shop.auction.domain.purchase.repository.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;

    public List<Purchase> getPurchasesByMemberId(Long memberId) {
        return purchaseRepository.findByMemberId(memberId);
    }

//    public List<Purchase> getPurchasesByProductId(Long productId) {
//        return purchaseRepository.findByProductId(productId);
//    }

    public Purchase createPurchase(Purchase purchase) {
        return purchaseRepository.save(purchase);
    }
}
