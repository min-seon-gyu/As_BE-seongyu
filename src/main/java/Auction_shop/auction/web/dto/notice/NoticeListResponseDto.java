package Auction_shop.auction.web.dto.notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeListResponseDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime date;
}
