package Auction_shop.auction.chatRoom.service;

import Auction_shop.auction.chat.dto.ChatDto;
import Auction_shop.auction.chat.repository.ChatRepository;
import Auction_shop.auction.chatRoom.domain.ChatRoom;
import Auction_shop.auction.chatRoom.dto.ChatRoomListResponseDto;
import Auction_shop.auction.chatRoom.repository.ChatRoomRepository;
import Auction_shop.auction.domain.member.Member;
import Auction_shop.auction.domain.member.service.MemberService;
import Auction_shop.auction.domain.product.Product;
import Auction_shop.auction.domain.product.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final ProductService productService;
    private final MemberService memberService;
    /**
     * 채팅방 목록 가져오기
     * @return list(채팅방 목록)
     */
    @Override
    public List<ChatRoomListResponseDto> findChatRoomsByUserId(Long userId) {
        List<ChatRoom> list = chatRoomRepository.findByUserId(userId);
        List<ChatRoomListResponseDto> responseDtos = new ArrayList<>();

        for (ChatRoom chatRoom : list) {
            Long postId = chatRoom.getPostId();
            Product product = productService.findProductById(postId);
            String firstImageUrl = product.getImageUrls().get(0);

            Long yourId = chatRoom.getYourId();
            Member member = memberService.getById(yourId);
            String firstProfileUrl = member.getProfileImage().getAccessUrl();
            String nickname = member.getNickname();

            Long roomId = chatRoom.getRoomId();
            ChatDto latestChatInfo = chatRepository.findLatestChatByRoomId(roomId);
            String latestChatLog = latestChatInfo.getMessage();
            LocalDateTime latestChatTime = latestChatInfo.getCreatedAt();

            ChatRoomListResponseDto responseDto = new ChatRoomListResponseDto();
            responseDto.setUserId(userId);
            responseDto.setYourId(yourId);
            responseDto.setPostId(postId);
            responseDto.setRoomId(roomId);

            responseDto.setImageUrl(firstImageUrl);
            responseDto.setProfileUrl(firstProfileUrl);
            responseDto.setNickname(nickname);
            responseDto.setLatestChatTime(latestChatTime);
            responseDto.setLatestChatLog(latestChatLog);

            responseDtos.add(responseDto);
        }

        return responseDtos;
    }

    /**
     * 채팅방 존재유무 확인
     * @return Optional.empty() or 조회된 객체
     */
    @Override
    public Optional<ChatRoom> findChatRoomInfo(Long userId, Long yourId, Long postId) {
        return Optional.ofNullable(chatRoomRepository.findByUserIdAndYourIdAndPostId(userId, yourId, postId));
    }

    /**
     * 채팅방 생성
     * @return roomId(생성한 채팅방 번호)
     */
    @Override
    public Long createNewChatRoom(Long userId, Long yourId, Long postId) {
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
