package Auction_shop.auction.web.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberUpdateDto {
    private String name;
    private String nickname;
    private String email;
    private String phone;
    private String address;
    private String detailAddress;
    private String zipcode;
    private List<String> categories;
}
