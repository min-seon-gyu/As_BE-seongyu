package Auction_shop.auction.web.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberUpdateDto {
    private String name;
    private String phone;
    private String address;
    private String detailAddress;
}
