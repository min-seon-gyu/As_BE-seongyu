package Auction_shop.auction.product.service;

import Auction_shop.auction.domain.image.Image;
import Auction_shop.auction.domain.image.service.ImageService;
import Auction_shop.auction.domain.member.Member;
import Auction_shop.auction.domain.member.service.MemberService;
import Auction_shop.auction.product.domain.Product;
import Auction_shop.auction.product.dto.ProductDto;
import Auction_shop.auction.product.dto.ProductListResponseDto;
import Auction_shop.auction.product.dto.ProductResponseDto;
import Auction_shop.auction.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    private final ImageService imageService;
    private final MemberService memberService;

    @Override
    public ProductResponseDto save(ProductDto productDto, Long memberId, List<MultipartFile> images) {
        Member member = memberService.getById(memberId);

        // DTO를 엔티티로 변환
        Product product = Product.builder()
                .title(productDto.getTitle())
                .member(member)
                .product_type(productDto.getProduct_type())
                .trade(productDto.getTrade())
                .initial_price(productDto.getInitial_price())
                .startTime(productDto.getStartTime())
                .endTime(productDto.getEndTime())
                .updateTime(productDto.getStartTime())
                .isSold(false)
                .minimum_price(productDto.getMinimum_price())
                .details(productDto.getDetails())
                .build();

        member.addProduct(product);

        List<Image> imageList = imageService.saveImages(images);
        product.setImageList(imageList);

        // 엔티티 저장
        Product savedProduct = productRepository.save(product);

        // 엔티티를 응답DTO로 변환 후 리턴
        ProductResponseDto responseDto = ProductResponseDto.builder()
                .product_id(savedProduct.getProduct_id())
                .title(savedProduct.getTitle())
                .product_type(savedProduct.getProduct_type())
                .trade(savedProduct.getTrade())
                .initial_price(savedProduct.getInitial_price())
                .minimum_price(savedProduct.getMinimum_price())
                .startTime(savedProduct.getStartTime())
                .endTime(savedProduct.getEndTime())
                .details(savedProduct.getDetails())
                .imageUrls(savedProduct.getImageUrls())
                .build();

        return responseDto;
    }

    @Override
    public List<ProductListResponseDto> findAllProduct(){
        List<Product> products = productRepository.findAll();
        List<ProductListResponseDto> collect = products.stream()
                .map(product -> {
                    String imageUrl = null;
                    if(!product.getImageList().isEmpty()){
                        imageUrl = product.getImageList().get(0).getAccessUrl();
                    }
                    ProductListResponseDto dto = ProductListResponseDto.builder()
                            .product_id(product.getProduct_id())
                            .title(product.getTitle())
                            .initial_price(product.getInitial_price())
                            .isSold(product.isSold())
                            .imageUrl(imageUrl)
                            .build();
                    return dto;
                })
                .collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<ProductListResponseDto> findAllByMemberId(Long memberId){
        List<Product> products = productRepository.findAllByMemberId(memberId);
        List<ProductListResponseDto> collect = products.stream()
                .map(product -> {
                    ProductListResponseDto dto = ProductListResponseDto.builder()
                            .product_id(product.getProduct_id())
                            .title(product.getTitle())
                            .initial_price(product.getInitial_price())
                            .isSold(product.isSold())
                            .build();
                    return dto;
                })
                .collect(Collectors.toList());
        return collect;
    }

    @Override
    public ProductResponseDto findProductById(Long product_id) {
        Optional<Product> product = productRepository.findById(product_id);
        if (product.isPresent()) {
            Product findProduct = product.get();
            ProductResponseDto responseDto = ProductResponseDto.builder()
                    .product_id(findProduct.getProduct_id())
                    .title(findProduct.getTitle())
                    .product_type(findProduct.getProduct_type())
                    .trade(findProduct.getTrade())
                    .initial_price(findProduct.getInitial_price())
                    .details(findProduct.getDetails())
                    .isSold(findProduct.isSold())
                    .imageUrls(findProduct.getImageUrls())
                    .build();

            return responseDto;
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public ProductResponseDto updateProductById(ProductDto productDto, Long product_id, List<MultipartFile> images) {
        Product product = productRepository.findById(product_id)
                .orElseThrow(() -> new IllegalArgumentException(product_id + "에 해당하는 물건이 없습니다."));

        if (product.getImageUrls() != null){
            for (Image image : product.getImageList()){
                imageService.deleteImage(image.getStoredName());
            }
            product.getImageList().clear();
        }

        List<Image> imageList = imageService.saveImages(images);
        product.getImageList().addAll(imageList);

        product.updateProduct(productDto.getTitle(), productDto.getProduct_type(), productDto.getDetails());

        ProductResponseDto productResponseDto = ProductResponseDto.builder()
                .product_id(product_id)
                .title(product.getTitle())
                .product_type(product.getProduct_type())
                .trade(product.getTrade())
                .initial_price(product.getInitial_price())
                .details(product.getDetails())
                .imageUrls(product.getImageUrls())
                .build();

        return productResponseDto;
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

    @Override
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void updateProductPrices(){
        LocalDateTime currentTime = LocalDateTime.now();
        List<Product> items = productRepository.findActiveProduct(currentTime);
        for (Product product : items){
            if (product.getUpdateTime().plusHours(1).isBefore(currentTime)) {
                int newPrice = product.getCurrent_price() - (product.getInitial_price() / (int) product.getTotalHours());
                if (newPrice < product.getMinimum_price()) {
                    newPrice = product.getMinimum_price();
                }
                product.updateCurrentPrice(newPrice);
                product.updateTime(currentTime);
            }
        }
        productRepository.saveAll(items);
    }
}
