package Auction_shop.auction.chatRoom.dto;

import Auction_shop.auction.chat.dto.ChatDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChatRoomInfoResponseDto {

    List<ChatDto> chatLog;
    int currentPrice;
    String title;
}
