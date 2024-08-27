package Auction_shop.auction.chatRoom.service;

import Auction_shop.auction.chat.dto.ChatDto;
import Auction_shop.auction.chat.repository.ChatRepository;
import Auction_shop.auction.chatRoom.domain.ChatRoom;
import Auction_shop.auction.chatRoom.repository.ChatRoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;

    /**
     * 채팅방 목록 가져오기
     * @return list(채팅방 목록)
     */
    @Override
    public List<ChatRoom> findChatRoomsByUserId(String userId) {
        List<ChatRoom> list = chatRoomRepository.findByUserId(userId);
        return list;
    }

    /**
     * 채팅방 존재유무 확인
     * @return Optional.empty() or 조회된 객체
     */
    @Override
    public Optional<ChatRoom> findChatRoomInfo(String userId, String yourId, String postId) {
        return Optional.ofNullable(chatRoomRepository.findByUserIdAndYourIdAndPostId(userId, yourId, postId));
    }

    /**
     * 채팅방 생성
     * @return roomId(생성한 채팅방 번호)
     */
    @Override
    public Long createNewChatRoom(String userId, String yourId, String postId) {
        Long roomId = chatRoomRepository.findChatRoomSize() + 1;
        ChatRoom myChatRoomList = ChatRoom.builder().userId(userId).yourId(yourId).postId(postId).roomId(roomId).build();
        ChatRoom yourChatRoomList = ChatRoom.builder().userId(yourId).yourId(userId).postId(postId).roomId(roomId).build();
        chatRoomRepository.save(myChatRoomList);
        chatRoomRepository.save(yourChatRoomList);
        return roomId;
    }

    /**
     * roomId를 가진 채팅방의 채팅 내역 전달
     * @return chatLog(채팅 내역)
     */
    @Override
    public List<ChatDto> fetchChatLog(Long roomId) {
        List<ChatDto> chatLog = chatRepository.findByRoomId(roomId);
        return chatLog;
    }
}
