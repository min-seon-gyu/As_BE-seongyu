package Auction_shop.auction.web.dto.inquiry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InquiryListResponseDto {

    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private boolean status;
}
