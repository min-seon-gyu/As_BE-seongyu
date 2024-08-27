package Auction_shop.auction.chatRoom.repository;

import Auction_shop.auction.chatRoom.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findByUserId(String userId);
    ChatRoom findByUserIdAndYourIdAndPostId(String userId, String yourId, String postId);

    @Query("SELECT COUNT(CR) FROM ChatRoom CR")
    Long findChatRoomSize();
}
