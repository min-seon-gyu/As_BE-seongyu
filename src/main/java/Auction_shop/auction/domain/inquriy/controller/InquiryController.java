package Auction_shop.auction.domain.inquriy.controller;

import Auction_shop.auction.domain.inquriy.Inquiry;
import Auction_shop.auction.domain.inquriy.service.InquiryService;
import Auction_shop.auction.web.dto.inquiry.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inquiry")
public class InquiryController {

    private final InquiryService inquiryService;
    private final InquiryMapper inquiryMapper;

    //등록
    @PostMapping
    public ResponseEntity<InquiryResponseDto> createInquiry(
            @RequestParam Long memberId,
            @RequestPart("inquiry") final InquiryCreateDto inquiryDto,
            @RequestPart(value = "images", required = false) final List<MultipartFile> images){
        Inquiry inquiry = inquiryService.createInquiry(inquiryDto, memberId, images);
        InquiryResponseDto collect = inquiryMapper.toResponseDto(inquiry);
        return ResponseEntity.status(HttpStatus.CREATED).body(collect);
    }

    //전체 조회(어드민)
    @GetMapping()
    public ResponseEntity<List<InquiryListResponseDto>> getAllInquiry(){
        List<Inquiry> inquiries = inquiryService.getAllInquiry();

        if (inquiries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<InquiryListResponseDto> collect = inquiries.stream()
                .map(inquiryMapper::toListResponseDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(collect);
    }

    //멤버 문의 조회
    @GetMapping("/member")
    public ResponseEntity<List<InquiryListResponseDto>> getAllByMemberId(@RequestParam Long memberId){
        List<Inquiry> inquiries = inquiryService.getAllByMemberId(memberId);
        if (inquiries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<InquiryListResponseDto> collect = inquiries.stream()
                .map(inquiryMapper::toListResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(collect);
    }

    //개별 조회
    @GetMapping("/{inquiryId}")
    public ResponseEntity<InquiryResponseDto> getByInquiryId(@PathVariable Long inquiryId){
        Inquiry inquiry = inquiryService.getById(inquiryId);
        InquiryResponseDto collect = inquiryMapper.toResponseDto(inquiry);

        return ResponseEntity.ok(collect);
    }

    //수정
    @PutMapping("/{inquiryId}")
    public ResponseEntity<InquiryResponseDto> updateInquiry(
            @PathVariable Long inquiryId, @RequestPart(value = "inquiry") InquiryUpdateDto inquiryDto,
            @RequestPart(value = "images", required = false) final List<MultipartFile> images){
        Inquiry inquiry = inquiryService.updateInquiry(inquiryId, inquiryDto, images);
        InquiryResponseDto collect = inquiryMapper.toResponseDto(inquiry);

        return ResponseEntity.ok(collect);
    }

    //삭제
    @DeleteMapping("/{inquiryId}")
    public ResponseEntity<Void> deleteInquiry(@PathVariable Long inquiryId){
        inquiryService.deleteInquiry(inquiryId);
        return ResponseEntity.noContent().build();
    }
}
