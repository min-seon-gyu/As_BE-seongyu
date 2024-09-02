package Auction_shop.auction.domain.product.controller;

import Auction_shop.auction.domain.like.service.LikeService;
import Auction_shop.auction.domain.product.ProductDocument;
import Auction_shop.auction.security.jwt.JwtUtil;
import Auction_shop.auction.web.dto.product.ProductListResponseDto;
import Auction_shop.auction.domain.product.service.ProductService;
import Auction_shop.auction.web.dto.product.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@Slf4j
@RequiredArgsConstructor
public class HistoryController {

    private final JwtUtil jwtUtil;
    private final ProductService productService;
    private final LikeService likeService;
    private final ProductMapper productMapper;

    //판매 내역 조회
    @GetMapping("/sell")
    public ResponseEntity<List<ProductListResponseDto>> getSellList(@RequestHeader("Authorization") String authorization
    ){
        Long memberId = jwtUtil.extractMemberId(authorization);
        Iterable<ProductDocument> products = productService.findAllByMemberId(memberId);
        List<Long> likedProductsIds = likeService.getLikeItems(memberId);

        List<ProductListResponseDto> collect = StreamSupport.stream(products.spliterator(), false)
                .map(product -> productMapper.toListResponeDto(product, likedProductsIds.contains(product.getId())))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(collect);
    }

}
