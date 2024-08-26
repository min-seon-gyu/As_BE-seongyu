package Auction_shop.auction.domain.product.controller;

import Auction_shop.auction.domain.like.service.LikeService;
import Auction_shop.auction.domain.product.Product;
import Auction_shop.auction.security.jwt.JwtUtil;
import Auction_shop.auction.web.dto.product.*;
import Auction_shop.auction.domain.product.service.ProductService;
import Auction_shop.auction.domain.product.validation.ProductValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final LikeService likeService;
    private final ProductMapper productMapper;
    private final ProductValidator productValidator;
    private final JwtUtil jwtUtil;

    @Autowired
    public ProductController(ProductService productService, LikeService likeService, ProductValidator productValidator, ProductMapper productMapper, JwtUtil jwtUtil) {
        this.productService = productService;
        this.likeService = likeService;
        this.productValidator = productValidator;
        this.productMapper = productMapper;
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
            Product product = productService.save(productDto, memberId, images);
            ProductResponseDto responseDto = productMapper.toResponseDto(product);
            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
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
        List<Product> products = productService.findAllProduct(memberId);
        List<Long> likedProductsIds = likeService.getLikeItems(memberId);

        if (products == null || products.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<ProductListResponseDto> collect = products.stream()
                .map(product -> productMapper.toListResponeDto(product, likedProductsIds.contains(product.getProduct_id())))
                .collect(Collectors.toList());

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