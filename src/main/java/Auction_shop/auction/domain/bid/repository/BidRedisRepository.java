package Auction_shop.auction.domain.bid.repository;

import Auction_shop.auction.domain.bid.Bid;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class BidRedisRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public void save(Bid bid) {
        String key = "bids" + bid.getProductId();
        String memberKey = "bids:member:" + bid.getMemberId();
        try {
            String jsonBid = objectMapper.writeValueAsString(bid); // Bid 객체를 JSON 문자열로 변환
            redisTemplate.opsForList().rightPush(key, jsonBid); // JSON 문자열로 저장
            redisTemplate.opsForList().rightPush(memberKey, jsonBid);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // 예외 처리
        }
    }

    public List<Bid> findBidsByProductId(Long productId) {
        String key = "bids" + productId;
        List<String> jsonBids = redisTemplate.opsForList().range(key, 0, -1); // JSON 문자열 리스트 가져오기

        return jsonBids.stream()
                .map(json -> {
                    try {
                        return objectMapper.readValue(json, Bid.class); // JSON 문자열을 Bid 객체로 변환
                    } catch (JsonProcessingException e) {
                        e.printStackTrace(); // 예외 처리
                        return null; // 변환 실패 시 null 반환
                    }
                })
                .collect(Collectors.toList());
    }

    public List<Bid> findBidsByMemberId(Long memberId) {
        String key = "bids:member:" + memberId;
        List<String> jsonBids = redisTemplate.opsForList().range(key, 0, -1); // JSON 문자열 리스트 가져오기

        return jsonBids.stream()
                .map(json -> {
                    try {
                        return objectMapper.readValue(json, Bid.class); // JSON 문자열을 Bid 객체로 변환
                    } catch (JsonProcessingException e) {
                        e.printStackTrace(); // 예외 처리
                        return null; // 변환 실패 시 null 반환
                    }
                })
                .collect(Collectors.toList());
    }

    //미사용 함수 ) 추후에 사용 할 시 주석 해제
//    public Bid findHighestBidByProductId(Long productId) {
//        String key = "bids" + productId;
//
//        Long size = redisTemplate.opsForList().size(key);
//
//        if (size == null || size == 0) {
//            return null; // 리스트가 비어있으면 null 반환
//        }
//
//        String latestJsonBid = redisTemplate.opsForList().index(key, size - 1);
//
//        if (latestJsonBid == null) {
//            return null; // 없으면 null 반환
//        }
//
//        try {
//            return objectMapper.readValue(latestJsonBid, Bid.class); // JSON 문자열을 Bid 객체로 변환
//        } catch (JsonProcessingException e) {
//            e.printStackTrace(); // 예외 처리
//            return null; // 변환 실패 시 null 반환
//        }
//    }
}
