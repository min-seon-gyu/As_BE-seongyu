package Auction_shop.auction.domain.product.controller;

import Auction_shop.auction.security.jwt.JwtUtil;
import Auction_shop.auction.web.dto.product.ProductDto;
import Auction_shop.auction.web.dto.product.ProductListResponseDto;
import Auction_shop.auction.web.dto.product.ProductResponseDto;
import Auction_shop.auction.domain.product.service.ProductService;
import Auction_shop.auction.domain.product.validation.ProductValidator;
import Auction_shop.auction.web.dto.product.ProductUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final ProductValidator productValidator;
    private final JwtUtil jwtUtil;

    @Autowired
    public ProductController(ProductService productService, ProductValidator productValidator, JwtUtil jwtUtil) {
        this.productService = productService;
        this.productValidator = productValidator;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 상품 등록
     */
    @PostMapping("/registration")
    public ResponseEntity<Object> createProduct(@RequestHeader("Authorization") String authorization,
            @RequestPart(value = "product") ProductDto productDto,
            @RequestPart(value = "images", required = false) final List<MultipartFile> images,
            BindingResult bindingResult) {
        productValidator.validate(productDto, bindingResult);
        log.info("bindingResult={}", bindingResult);

        if (bindingResult.hasErrors()) {
            // 클라이언트 에러 400
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong Type : " + bindingResult.getFieldError().getDefaultMessage());
        }
        Long memberId = jwtUtil.extractMemberId(authorization);
        try {
            ProductResponseDto productResponseDto = productService.save(productDto, memberId, images);
            return ResponseEntity.status(HttpStatus.OK).body(productResponseDto);
        } catch (Exception e) {
            // 서버 에러 500
            log.info("error={}", e.getMessage(), ProductController.class);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error");
        }
    }

    /**
     * 상품 조회
     */
    @GetMapping()
    public ResponseEntity<Object> getAllProduct(@RequestHeader("Authorization") String authorization){
        Long memberId = jwtUtil.extractMemberId(authorization);
        List<ProductListResponseDto> collect = productService.findAllProduct(memberId);
        if (collect == null){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(collect);
    }


    /**
     * 상품 상세 조회
     */
    @GetMapping("/search/{product_id}")
    public ResponseEntity<Object> getProductById(@RequestHeader("Authorization") String authorization, @PathVariable Long product_id) {
        Long memberId = jwtUtil.extractMemberId(authorization);
        ProductResponseDto productResponseDto = productService.findProductById(memberId, product_id);
        if (productResponseDto == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error - Product not found, product_id doesn't exist in Database :(");
        } else {
            String name = jwtUtil.extractNickname(authorization);
            boolean isOwner = productResponseDto.getCreatedBy().equals(name);
            productResponseDto.setIsOwner(isOwner);
            return ResponseEntity.status(HttpStatus.OK).body(productResponseDto);
        }
    }

    /**
     * 상품 수정
     */
    @PutMapping("/update/{product_id}")
    public ResponseEntity<Object> updateProductById(
            @PathVariable Long product_id, @RequestPart(value = "product") ProductUpdateDto productUpdateDto,
            @RequestPart(value = "images", required = false) final List<MultipartFile> images, BindingResult bindingResult) {
//        productValidator.validate(productUpdateDto, bindingResult);
//        log.info("bindingResult={}", bindingResult);

//        if (bindingResult.hasErrors()) {
//            // 클라이언트 에러 400
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong Type : " + bindingResult.getFieldError().getDefaultMessage());
//        }
        try {
            ProductResponseDto productResponseDto = productService.updateProductById(productUpdateDto, product_id, images);
            if (productResponseDto == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("product_id doesn't exist in Database");
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(productResponseDto);
            }
        } catch (Exception e) {
            // 서버 에러 500
            log.info("error={}", e.getMessage(), ProductController.class);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update failed. check product_id, productDto and Database");
        }
    }


    /**
     * 상품 삭제
     */
    @DeleteMapping("/delete/{product_id}")
    public ResponseEntity<Object> deleteProductById(@PathVariable Long product_id) {
        boolean isFound = productService.deleteProductById(product_id);
        if (isFound) {
            return ResponseEntity.status(HttpStatus.OK).body("delete success");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Delete failed. check product_id and Database");
        }
    }
}