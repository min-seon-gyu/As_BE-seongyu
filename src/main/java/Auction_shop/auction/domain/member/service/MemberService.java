package Auction_shop.auction.domain.member.service;

import Auction_shop.auction.domain.member.Member;
import Auction_shop.auction.domain.member.repository.MemberRepository;
import Auction_shop.auction.web.dto.MemberUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public Member save(String uuid){
        Member member = Member.builder()
                .uuid(uuid)
                .role("USER")
                .name("TEST")
                .phone("TEST")
                .point(0l)
                .build();
        memberRepository.save(member);
        return member;
    }

    public Member getByUuid(String uuid){
        Optional<Member> findMember = memberRepository.findByUuid(uuid);
        if(findMember.isEmpty()) return null;

        return findMember.get();
    }

    public Member getById(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException(memberId + "에 해당하는 회원이 없습니다."));
        return member;
    }

    @Transactional
    public Member updateMember(Long memberId, MemberUpdateDto memberUpdateDto){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException(memberId + "에 해당하는 회원이 없습니다."));
        member.update(memberUpdateDto.getName(), memberUpdateDto.getPhone(), memberUpdateDto.getAddress(), memberUpdateDto.getDetailAddress());
        return member;
    }

    @Transactional
    public void deleteMember(Long memberId) {
        memberRepository.deleteById(memberId);
    }
}
