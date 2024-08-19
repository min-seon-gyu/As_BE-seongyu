package Auction_shop.auction.web.dto.block;

import Auction_shop.auction.domain.member.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlockResponseDto {
    private Long id;;
    private Long BlockedMemberId;
    private String BlockedMemberName;
    private String BlockedMemberPhone;
    private Address BlockedMemberAddress;
}
