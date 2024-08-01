package Auction_shop.auction.domain.product.service;

import Auction_shop.auction.web.dto.product.ProductDto;
import Auction_shop.auction.web.dto.product.ProductListResponseDto;
import Auction_shop.auction.web.dto.product.ProductResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    ProductResponseDto save(ProductDto productDto, Long memberId, List<MultipartFile> images);
    List<ProductListResponseDto> findAllProduct();
    List<ProductListResponseDto> findAllByMemberId(Long MemberId);
    ProductResponseDto findProductById(Long product_id);
    ProductResponseDto updateProductById(ProductDto productDto, Long product_id, List<MultipartFile> images);
    ProductResponseDto purchaseProductItem(Long product_id);
    boolean deleteProductById(Long product_id);
    void updateProductPrices();
}
