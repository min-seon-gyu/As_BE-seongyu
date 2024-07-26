package Auction_shop.auction.web.dto.inquiry;

import Auction_shop.auction.domain.inquriy.Inquiry;

public interface InquiryMapper {

    InquiryResponseDto toResponseDto(Inquiry inquiry);
    InquiryListResponseDto toListResponseDto(Inquiry inquiry);
}
