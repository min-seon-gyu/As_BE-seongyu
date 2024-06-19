package Auction_shop.auction.domain.inquriy.controller;

import Auction_shop.auction.domain.inquriy.Inquiry;
import Auction_shop.auction.domain.inquriy.service.InquiryService;
import Auction_shop.auction.web.dto.InquiryCreateDto;
import Auction_shop.auction.web.dto.InquiryListResponseDto;
import Auction_shop.auction.web.dto.InquiryResponseDto;
import Auction_shop.auction.web.dto.InquiryUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inquiry")
public class InquiryController {

    private final InquiryService inquiryService;

    //등록
    @PostMapping
    public ResponseEntity<Inquiry> createInquiry(@RequestBody final InquiryCreateDto inquiryDto){
        Inquiry inquiry = inquiryService.createInquiry(inquiryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(inquiry);
    }

    //전체 조회
    @GetMapping()
    public ResponseEntity<List<InquiryListResponseDto>> getAllInquiry(){
        List<Inquiry> inquiries = inquiryService.getAllInquiry();

        if (inquiries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<InquiryListResponseDto> collect = inquiries.stream()
                .map(inquiry -> {
                    InquiryListResponseDto dto = InquiryListResponseDto.builder()
                            .id(inquiry.getId())
//                            .member(inquiry.getMember())
                            .title(inquiry.getTitle())
                            .build();
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(collect);
    }

    //개별 조회
    @GetMapping("/{inquiryId}")
    public ResponseEntity<InquiryResponseDto> getByInquiryId(@PathVariable Long inquiryId){
        Inquiry inquiry = inquiryService.getById(inquiryId);
        InquiryResponseDto collect = InquiryResponseDto.builder()
                .id(inquiry.getId())
                .title(inquiry.getTitle())
                .content(inquiry.getContent())
                .status(inquiry.isStatus())
//                .member(inquiry.getMember.getName())
                .build();

        return ResponseEntity.ok(collect);
    }

    //멤버 문의 조회
    //멤버 엔티티 구현 되면 수정 예정
//    @GetMapping(/"memberId")
//    public ResponseEntity<List<InquiryResponseDto>> getAllByMemberId(){
//        List<Inquiry> inquiries = inquiryService.getAllByMemberId();
//        List<InquiryResponseDto> collect = inquiries.stream()
//                .map(inquiry -> new InquiryResponseDto())
//                .collect(toList());
//        return ResponseEntity.ok(collect);
//    }

    //수정
    @PutMapping("/{inquiryId}")
    public ResponseEntity<InquiryResponseDto> updateInquiry(@PathVariable Long inquiryId, InquiryUpdateDto inquiryDto){
        Inquiry inquiry = inquiryService.updateInquiry(inquiryId, inquiryDto);
        System.out.println("inquiry.getId() = " + inquiry.getId());
        System.out.println("inquiry.getTitle() = " + inquiry.getTitle());
        System.out.println("inquiry.getContent() = " + inquiry.getContent());
        InquiryResponseDto collect = InquiryResponseDto.builder()
                .id(inquiry.getId())
                .title(inquiry.getTitle())
                .content(inquiry.getContent())
//                .member(inquiry.getMember.getName())
                .build();

        return ResponseEntity.ok(collect);
    }

    //삭제
    @DeleteMapping("/{inquiryId}")
    public ResponseEntity<Void> deleteInquiry(@PathVariable Long inquiryId){
        inquiryService.deleteInquiry(inquiryId);
        return ResponseEntity.noContent().build();
    }
}
