package Auction_shop.auction.product.service;

import Auction_shop.auction.product.dto.ProductDto;
import Auction_shop.auction.product.dto.ProductResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    ProductResponseDto save(ProductDto productDto, List<MultipartFile> images);
    ProductResponseDto findProductById(Long product_id);
    ProductResponseDto updateProductById(ProductDto productDto, Long product_id);
    boolean deleteProductById(Long product_id);
}
