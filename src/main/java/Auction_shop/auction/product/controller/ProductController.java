package Auction_shop.auction.product.controller;

import Auction_shop.auction.product.dto.ProductDto;
import Auction_shop.auction.product.dto.ProductListResponseDto;
import Auction_shop.auction.product.dto.ProductResponseDto;
import Auction_shop.auction.product.service.ProductService;
import Auction_shop.auction.product.validation.ProductValidator;
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

    @Autowired
    public ProductController(ProductService productService, ProductValidator productValidator) {
        this.productService = productService;
        this.productValidator = productValidator;
    }

    /**
     * 상품 등록
     */
    @PostMapping("/registration")
    public ResponseEntity<Object> createProduct(
            @RequestPart(value = "product") ProductDto productDto,
            @RequestPart(value = "images", required = false) final List<MultipartFile> images,
            BindingResult bindingResult) {
        productValidator.validate(productDto, bindingResult);
        log.info("bindingResult={}", bindingResult);

        if (bindingResult.hasErrors()) {
            // 클라이언트 에러 400
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong Type : " + bindingResult.getFieldError().getDefaultMessage());
        }
        try {
            ProductResponseDto productResponseDto = productService.save(productDto, images);
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
    public ResponseEntity<Object> getAllProduct(){
        List<ProductListResponseDto> collect = productService.findAllProduct();
        if (collect == null){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(collect);
    }


    /**
     * 상품 상세 조회
     */
    @GetMapping("/search/{product_id}")
    public ResponseEntity<Object> getProductById(@PathVariable Long product_id) {
        ProductResponseDto productResponseDto = productService.findProductById(product_id);
        if (productResponseDto == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error - Product not found, product_id doesn't exist in Database :(");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(productResponseDto);
        }
    }

    /**
     * 상품 수정
     */
    @PutMapping("/update/{product_id}")
    public ResponseEntity<Object> updateProductById(
            @PathVariable Long product_id, @RequestPart(value = "product") ProductDto productDto,
            @RequestPart(value = "images", required = false) final List<MultipartFile> images, BindingResult bindingResult) {
        productValidator.validate(productDto, bindingResult);
        log.info("bindingResult={}", bindingResult);

        if (bindingResult.hasErrors()) {
            // 클라이언트 에러 400
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong Type : " + bindingResult.getFieldError().getDefaultMessage());
        }
        try {
            ProductResponseDto productResponseDto = productService.updateProductById(productDto, product_id, images);
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