package Auction_shop.auction.chat.dto;

import java.time.LocalDateTime;

public class ChatDto {
    private Long id;
    private Long roomId; // 채팅방번호
    private String 유저ID;
    private String 메세지;
    private LocalDateTime 생성시간;
}
