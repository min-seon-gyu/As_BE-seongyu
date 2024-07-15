package Auction_shop.auction.web.dto;

import Auction_shop.auction.domain.member.Address;
import Auction_shop.auction.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {
    private Long id;
    private String username;
    private String name;
    private Address address;
    private String phone;
    private Long point;
    private boolean available;
    private String role;

    public static MemberResponseDto create(Member member){
        return MemberResponseDto.builder()
                .id(member.getId())
                .username(member.getUuid())
                .name(member.getName())
                .address(member.getAddress())
                .phone(member.getPhone())
                .point(member.getPoint())
                .role(member.getRole())
                .available(member.isAvailable())
                .build();
    }
}
