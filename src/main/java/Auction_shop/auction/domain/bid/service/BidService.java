package Auction_shop.auction.domain.bid.service;

import Auction_shop.auction.domain.bid.Bid;
import Auction_shop.auction.domain.bid.repository.BidJpaRepository;
import Auction_shop.auction.domain.bid.repository.BidRedisRepository;
import Auction_shop.auction.domain.product.Product;
import Auction_shop.auction.domain.product.ProductDocument;
import Auction_shop.auction.domain.product.elasticRepository.ProductElasticsearchRepository;
import Auction_shop.auction.domain.product.repository.ProductJpaRepository;
import Auction_shop.auction.web.dto.product.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BidService {
    private final BidRedisRepository bidRedisRepository;
    private final BidJpaRepository bidJpaRepository;

    public List<Bid> getBidsForProduct(Long productId){
        List<Bid> bids = bidRedisRepository.findBidsByProductId(productId);

        if (bids.isEmpty()){
            bids = bidJpaRepository.findByProductId(productId);
            for(Bid bid : bids){
                System.out.println("bid.getAmount() = " + bid.getAmount());
                bidRedisRepository.save(bid);
            }
            Collections.reverse(bids);
        }

        return bids;
    }

    public Bid getHighestBidForProduct(Long productId){
        Bid highestBid = bidRedisRepository.findHighestBidByProductId(productId);

        if (highestBid == null){
            highestBid = bidJpaRepository.findHighestBidByProductId(productId);
        }

        return highestBid;
    }
}
