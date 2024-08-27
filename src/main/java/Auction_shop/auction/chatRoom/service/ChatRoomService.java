package Auction_shop.auction.chatRoom.service;

import Auction_shop.auction.chat.dto.ChatDto;
import Auction_shop.auction.chatRoom.domain.ChatRoom;
import java.util.List;
import java.util.Optional;

public interface ChatRoomService {
    List<ChatRoom> findChatRoomsByUserId(String userId);
    Optional<ChatRoom> findChatRoomInfo(String userId, String yourId, String postId);
    Long createNewChatRoom(String userId, String yourId, String postId);
    List<ChatDto> fetchChatLog(Long roomId);
}
