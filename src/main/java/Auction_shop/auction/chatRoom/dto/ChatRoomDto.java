package Auction_shop.auction.chatRoom.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatRoomDto {

    private String userId;  // 유저ID
    private String postId;  // 게시물ID
    private String yourId;  // 상대방ID

    @Builder
    public ChatRoomDto(String userId, String postId, String yourId) {
        this.userId = userId;
        this.postId = postId;
        this.yourId = yourId;
    }
}
