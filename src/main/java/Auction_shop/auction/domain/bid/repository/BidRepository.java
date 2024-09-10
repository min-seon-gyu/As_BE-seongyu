package Auction_shop.auction.domain.bid.repository;

import Auction_shop.auction.domain.bid.Bid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
//redis 사용
public class BidRepository {

    private RedisTemplate<String, Bid> redisTemplate;

    public void save(Bid bid) {
        String key = "bids" + bid.getProduct_id();
        redisTemplate.opsForList().rightPush(key, bid);
    }

    public List<Bid> findBidsByProductId(Long product_id) {
        String key = "bids" + product_id;
        return redisTemplate.opsForList().range(key, 0, -1);
    }
}
