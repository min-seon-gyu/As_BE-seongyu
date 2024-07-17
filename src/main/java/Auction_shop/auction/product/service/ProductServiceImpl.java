package Auction_shop.auction.product.service;

import Auction_shop.auction.domain.image.Image;
import Auction_shop.auction.domain.image.service.ImageService;
import Auction_shop.auction.product.domain.Product;
import Auction_shop.auction.product.dto.ProductDto;
import Auction_shop.auction.product.dto.ProductResponseDto;
import Auction_shop.auction.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    private final ImageService imageService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ImageService imageService) {
        this.productRepository = productRepository;
        this.imageService = imageService;
    }

    @Override
    public ProductResponseDto save(ProductDto productDto, List<MultipartFile> images) {
        // DTO를 엔티티로 변환
        Product product = Product.builder()
                .seller(productDto.getSeller())
                .title(productDto.getTitle())
                .product_type(productDto.getProduct_type())
                .trade(productDto.getTrade())
                .initial_price(productDto.getInitial_price())
                .details(productDto.getDetails())
                .build();

        List<Image> imageList = imageService.saveImages(images);
        product.setImageList(imageList);

        // 엔티티 저장
        Product savedProduct = productRepository.save(product);

        // 엔티티를 응답DTO로 변환 후 리턴
        ProductResponseDto responseDto = ProductResponseDto.builder()
                .product_id(savedProduct.getProduct_id())
                .seller(savedProduct.getSeller())
                .title(savedProduct.getTitle())
                .product_type(savedProduct.getProduct_type())
                .trade(savedProduct.getTrade())
                .initial_price(savedProduct.getInitial_price())
                .details(savedProduct.getDetails())
                .imageUrls(savedProduct.getImageUrls())
                .build();

        return responseDto;
    }

    @Override
    public ProductResponseDto findProductById(Long product_id) {
        Optional<Product> product = productRepository.findById(product_id);
        if (product.isPresent()) {
            Product findProduct = product.get();
            ProductResponseDto responseDto = ProductResponseDto.builder()
                    .product_id(findProduct.getProduct_id())
                    .seller(findProduct.getSeller())
                    .title(findProduct.getTitle())
                    .product_type(findProduct.getProduct_type())
                    .trade(findProduct.getTrade())
                    .initial_price(findProduct.getInitial_price())
                    .details(findProduct.getDetails())
                    .imageUrls(findProduct.getImageUrls())
                    .build();

            return responseDto;
        } else {
            return null;
        }
    }

    @Override
    public ProductResponseDto updateProductById(ProductDto productDto, Long product_id) {
        Optional<Product> product = productRepository.findById(product_id);
        if (product.isPresent()) {
            Product findProduct = product.get();

            Product updateProduct = Product.builder()
                    .product_id(findProduct.getProduct_id())
                    .seller(productDto.getSeller())
                    .title(productDto.getTitle())
                    .product_type(productDto.getProduct_type())
                    .trade(productDto.getTrade())
                    .initial_price(productDto.getInitial_price())
                    .details(productDto.getDetails())
                    .build();

            productRepository.save(updateProduct);

            ProductResponseDto productResponseDto = ProductResponseDto.builder()
                    .product_id(updateProduct.getProduct_id())
                    .seller(updateProduct.getSeller())
                    .title(updateProduct.getTitle())
                    .product_type(updateProduct.getProduct_type())
                    .trade(updateProduct.getTrade())
                    .initial_price(updateProduct.getInitial_price())
                    .details(updateProduct.getDetails())
                    .build();

            return productResponseDto;
        } else {
            return null;
        }
    }

    @Override
    public boolean deleteProductById(Long product_id) {
        boolean isFound = productRepository.existsById(product_id);
        Product product = productRepository.findById(product_id)
                .orElseThrow(() -> new IllegalArgumentException(product_id + "에 해당하는 물건이 없습니다."));
        if (isFound) {
            for (Image image : product.getImageList()){
                imageService.deleteImage(image.getStoredName());
            }
            productRepository.deleteById(product_id);
        }
        return isFound;
    }
}
