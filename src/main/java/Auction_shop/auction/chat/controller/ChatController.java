package Auction_shop.auction.chat.controller;

import Auction_shop.auction.chat.domain.Chat;
import Auction_shop.auction.chat.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    @Autowired
    public ChatController(SimpMessagingTemplate messagingTemplate, ChatService chatService) {
        this.messagingTemplate = messagingTemplate;
        this.chatService = chatService;
    }

    @MessageMapping("/chat/{roomId}")
    public void processMessage(@DestinationVariable String roomId, Chat chat) throws Exception {
        // NOTE Stomp는 메세지 전송을 위한 프로토콜로 pub - sub(발행 - 구독)을 통해 동작합니다
        // NOTE 웹소켓 연결 후 클라이언트에서 서버로 STOMP 연결 요청을 하고 성공 시 >>>CONNECTED\n id:~~\n destination:~~ 처럼 전달(frame 구조)
        // NOTE 이후 사용자1,사용자2가 특정 경로를 구독합니다. /sub/room/방번호3 (/sub/room 경로는 고정입니다)
        // NOTE 사용자3이 /pub/chat/방번호3의 경로로 메세지를 보내면 방번호3을 구독한 사용자1, 사용자2에게 메세지가 전달됩니다.

        String destination = "/sub/roomId/" + roomId;   // 도착 경로 지정
        chatService.createChat(chat);
        try {
            messagingTemplate.convertAndSend(destination, chat.get메세지());   // 도착 경로로 메세지 전달
        } catch (Exception e) {
            log.error("ChatError = {}", e);
            messagingTemplate.convertAndSend(destination, "Error:" + e.getMessage());
            /// TODO: API 명세서에 에러처리 내용 작성
        }
        /// TODO: 26일 회의 질문 사항 - 클라이언트에서 채팅내역 어떻게 관리하는지? 관리 어렵다면 채팅방 열 때 서버에서 이전 채팅 기록들 가져오도록 구현 추가
    }
}
