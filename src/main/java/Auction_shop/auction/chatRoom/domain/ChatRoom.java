package Auction_shop.auction.chatRoom.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;  // 나의 ID

    @Column(nullable = false)
    private String yourId;  // 상대방ID

    @Column(nullable = false)
    private String postId;  // 게시글ID

    private Long roomId;    // 채팅방번호

    @Builder
    public ChatRoom(String userId, String yourId, String postId, Long roomId) {
        this.userId = userId;
        this.yourId = yourId;
        this.postId = postId;
        this.roomId = roomId;
    }
}
