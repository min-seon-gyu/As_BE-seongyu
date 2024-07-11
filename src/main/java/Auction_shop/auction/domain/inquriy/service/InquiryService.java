package Auction_shop.auction.domain.inquriy.service;

import Auction_shop.auction.domain.image.Image;
import Auction_shop.auction.domain.image.service.ImageService;
import Auction_shop.auction.domain.inquriy.Inquiry;
import Auction_shop.auction.domain.inquriy.repository.InquiryRepository;
import Auction_shop.auction.web.dto.InquiryCreateDto;
import Auction_shop.auction.web.dto.InquiryUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
//Image 관련 내용은 InquiryImageService로 분리 예정
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final ImageService imageService;

    //문의 등록
    //User 관련 주석처리
    @Transactional
    public Inquiry createInquiry(InquiryCreateDto inquiryDto, List<MultipartFile> images){
//        Long memberId = inquiryDto.getId();
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new EntityNotFoundException(memberId + "에 해당하는 유저가 없습니다."));
        Inquiry inquiry = Inquiry.builder()
//                .member(member);
                .title(inquiryDto.getTitle())
                .content(inquiryDto.getContent())
                .status(false)
                .build();

        List<Image> imageList = imageService.saveImages(images);
        inquiry.setImageList(imageList);

        return inquiryRepository.save(inquiry);
    }

    //문의 전체 조회
    public List<Inquiry> getAllInquiry() {
        List<Inquiry> all = inquiryRepository.findAll();
        return inquiryRepository.findAll();
    }

    //문의 단일 조회
    public Inquiry getById(Long inquiryId){
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new IllegalArgumentException(inquiryId + "에 해당하는 문의가 없습니다."));
        return inquiry;
    }

    //유저 문의 조회
//    public List<Inquiry> getAllByMemberId(){
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new EntityNotFoundException(memberId + "에 해당하는 유저가 없습니다."));
//        return inquiryRepository.findByUserId(member.getId());
//    }

    //itemId로 문의글 찾기
    //유저끼리의 채팅이 있지만 제때 확인 못 할 경우를 대비해 아이템별 문의 기능 구현 준비
//    public List<Inquiry> findAllByItemId(Long userId){
//        return inquiryRepository.findByUserId(userId);
//    }

    //문의 게시글 업데이트
    @Transactional
    public Inquiry updateInquiry(Long inquiryId, InquiryUpdateDto inquiryDto){
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new IllegalArgumentException(inquiryId + "에 해당하는 게시글이 없습니다."));

        //기존 사진 삭제
        if (inquiry.getImageUrls() != null){
            for (Image image : inquiry.getImageList()){
                imageService.deleteImage(image.getStoredName());
            }
        }

        inquiry.updateInquiry(inquiryDto.getTitle(), inquiryDto.getContent());
        return inquiry;
    }

    //문의 게시글 삭제
    @Transactional
    public void deleteInquiry(Long inquiryId){
        inquiryRepository.deleteById(inquiryId);
    }

}
