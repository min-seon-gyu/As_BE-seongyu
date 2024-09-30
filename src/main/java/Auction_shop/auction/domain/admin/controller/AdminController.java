package Auction_shop.auction.domain.admin.controller;

import Auction_shop.auction.domain.inquriy.Inquiry;
import Auction_shop.auction.domain.inquriy.repository.InquiryRepository;
import Auction_shop.auction.domain.inquriy.service.InquiryService;
import Auction_shop.auction.domain.member.Member;
import Auction_shop.auction.domain.member.repository.MemberRepository;
import Auction_shop.auction.domain.product.ProductDocument;
import Auction_shop.auction.domain.product.service.ProductService;
import Auction_shop.auction.web.dto.admin.CreateAnswerDto;
import Auction_shop.auction.web.dto.inquiry.InquiryListResponseDto;
import Auction_shop.auction.web.dto.inquiry.InquiryMapper;
import Auction_shop.auction.web.dto.inquiry.InquiryResponseDto;
import Auction_shop.auction.web.dto.member.MemberListResponseDto;
import Auction_shop.auction.web.dto.member.MemberMapper;
import Auction_shop.auction.web.dto.product.ProductListResponseDto;
import Auction_shop.auction.web.dto.product.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminController {
    private final ProductService productService;
    private final InquiryService inquiryService;
    private final MemberRepository memberRepository;
    private final InquiryRepository inquiryRepository;
    private final MemberMapper memberMapper;
    private final InquiryMapper inquiryMapper;
    private final ProductMapper productMapper;

    //전체 회원 조회
    @GetMapping
    public ResponseEntity<List<MemberListResponseDto>> getAllMember(){
        List<Member> members = memberRepository.findAll();
        List<MemberListResponseDto> collect = members.stream()
                .map(memberMapper::toListResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(collect);
    }

    //전체 문의 조회
    @GetMapping
    public ResponseEntity<List<InquiryListResponseDto>> getAllInquiry(){
        List<Inquiry> inquiries = inquiryRepository.findAll();
        List<InquiryListResponseDto> collect = inquiries.stream()
                .map(inquiryMapper::toListResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(collect);
    }

    //전체 경매 조회
    @GetMapping
    public ResponseEntity<List<ProductListResponseDto>> getAllProduct(){
        Iterable<ProductDocument> products = productService.findAllProduct();
        List<ProductListResponseDto> collect = StreamSupport.stream(products.spliterator(), false)
                .map(product -> productMapper.toListResponseDto(product, false))
                .collect(Collectors.toList());
        return ResponseEntity.ok(collect);
    }

    //문의 답변
    @PostMapping("/inquiry/{id}")
    public ResponseEntity<InquiryResponseDto> addAnswer(@PathVariable("id") Long id, CreateAnswerDto createAnswerDto){
        Inquiry inquiry = inquiryService.addAnswer(id, createAnswerDto.getContent());
        InquiryResponseDto collect = inquiryMapper.toResponseDto(inquiry);
        return ResponseEntity.status(HttpStatus.CREATED).body(collect);
    }

    //회원 삭제
    @DeleteMapping("/member/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable("id") Long id){
        memberRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    //문의 삭제
    @DeleteMapping("/inquiry/{id}")
    public ResponseEntity<Void> deleteInquiry(@PathVariable("id") Long id){
        inquiryRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    //경매 삭제
    @DeleteMapping("/product/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id){
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }
}
