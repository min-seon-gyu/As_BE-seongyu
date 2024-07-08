package Auction_shop.auction.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InquiryResponseDto {

    private Long id;
    private String member;
    private String title;
    private String content;
    private boolean status;
    private List<String> imageUrls;
}
