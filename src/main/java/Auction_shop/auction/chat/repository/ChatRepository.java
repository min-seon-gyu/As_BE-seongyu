package Auction_shop.auction.chat.repository;

import Auction_shop.auction.chat.domain.Chat;
import Auction_shop.auction.chat.dto.ChatDto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ChatRepository extends MongoRepository<Chat, String> {
    List<ChatDto> findByRoomId(Long roomId);

    @Query(value = "{ 'roomId' : ?0 }", sort = "{ 'createdAt' : -1 }")
    ChatDto findLatestChatByRoomId(Long RoomId);
}
