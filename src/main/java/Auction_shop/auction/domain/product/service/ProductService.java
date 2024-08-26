package Auction_shop.auction.domain.product.service;

import Auction_shop.auction.web.dto.product.ProductDto;
import Auction_shop.auction.web.dto.product.ProductListResponseDto;
import Auction_shop.auction.web.dto.product.ProductResponseDto;
import Auction_shop.auction.web.dto.product.ProductUpdateDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    ProductResponseDto save(ProductDto productDto, Long memberId, List<MultipartFile> images);
    List<ProductListResponseDto> findAllProduct(Long memberId);
    List<ProductListResponseDto> findAllByMemberId(Long MemberId);
    ProductResponseDto findProductById(Long memberId, Long product_id);
    ProductResponseDto updateProductById(ProductUpdateDto productUpdateDto, Long product_id, List<MultipartFile> images);
    int findCurrentPriceById(Long productId);
    void purchaseProductItem(Long product_id);
    boolean deleteProductById(Long product_id);
    void updateProductPrices();
    void createDummyProducts(int count);
}
