package Auction_shop.auction.domain.member.controller;

import Auction_shop.auction.domain.member.Member;
import Auction_shop.auction.domain.member.service.MemberService;
import Auction_shop.auction.security.jwt.JwtUtil;
import Auction_shop.auction.web.dto.member.MemberResponseDto;
import Auction_shop.auction.web.dto.member.MemberUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    @GetMapping()
    public ResponseEntity<MemberResponseDto> getByMemberId(@RequestHeader("Authorization") String authorization){
        Long memberId = jwtUtil.extractMemberId(authorization);
        Member member = memberService.getById(memberId);
        return ResponseEntity.ok(MemberResponseDto.create(member));
    }

    @PatchMapping()
    public ResponseEntity<MemberResponseDto> updateMember(@RequestHeader("Authorization") String authorization,
                                                          @RequestPart(value = "member") MemberUpdateDto memberUpdateDto,
                                                          @RequestPart(value = "image", required = false) MultipartFile image){
        Long memberId = jwtUtil.extractMemberId(authorization);
        Member member = memberService.updateMember(memberId, memberUpdateDto, image);
        return ResponseEntity.ok(MemberResponseDto.create(member));
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteMember(@RequestHeader("Authorization") String authorization){
        Long memberId = jwtUtil.extractMemberId(authorization);
        memberService.deleteMember(memberId);
        return ResponseEntity.noContent().build();
    }
}
