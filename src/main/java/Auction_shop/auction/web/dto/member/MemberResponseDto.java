package Auction_shop.auction.web.dto.member;

import Auction_shop.auction.domain.image.Image;
import Auction_shop.auction.domain.member.Address;
import Auction_shop.auction.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {
    private Long id;
    private String username;
    private String name;
    private String nickname;
    private String email;
    private Address address;
    private String phone;
    private Long point;
    private boolean available;
    private String profileImageUrl;
    private String role;

    public static MemberResponseDto create(Member member){
        return MemberResponseDto.builder()
                .id(member.getId())
                .username(member.getUuid())
                .name(member.getName())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .address(member.getAddress())
                .phone(member.getPhone())
                .point(member.getPoint())
                .profileImageUrl(Optional.ofNullable(member.getProfileImage())
                        .map(Image::getAccessUrl)
                        .orElse(null))
                .role(member.getRole())
                .available(member.isAvailable())
                .build();
    }
}
