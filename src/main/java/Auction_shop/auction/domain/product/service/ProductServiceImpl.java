package Auction_shop.auction.domain.product.service;

import Auction_shop.auction.domain.image.Image;
import Auction_shop.auction.domain.image.service.ImageService;
import Auction_shop.auction.domain.like.service.LikeService;
import Auction_shop.auction.domain.member.Member;
import Auction_shop.auction.domain.member.service.MemberService;
import Auction_shop.auction.domain.product.repository.ProductRepository;
import Auction_shop.auction.domain.product.Product;
import Auction_shop.auction.web.dto.product.ProductDto;
import Auction_shop.auction.web.dto.product.ProductListResponseDto;
import Auction_shop.auction.web.dto.product.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    private final ImageService imageService;
    private final MemberService memberService;
    private final LikeService likeService;

    private final Random random = new Random();

    @Transactional
    public void createDummyProducts(int count) {
        for (int i = 0; i < count; i++) {
            Product product = Product.builder()
                    .title("Product " + (i + 1))
                    .conditions("New")
                    .categories(createRandomCategories())
                    .tradeTypes(createRandomTradeTypes())
                    .tradeLocation("Location " + random.nextInt(100))
                    .initial_price(random.nextInt(1000) + 100) // 최소 100
                    .minimum_price(random.nextInt(500) + 50) // 최소 50
                    .startTime(LocalDateTime.now().minusDays(random.nextInt(10)))
                    .endTime(LocalDateTime.now().plusDays(random.nextInt(10)))
                    .updateTime(LocalDateTime.now())
                    .isSold(false)
                    .details("This is a description for product " + (i + 1))
                    .build();

            productRepository.save(product);
        }
    }

    @Override
    @Transactional
    public ProductResponseDto save(ProductDto productDto, Long memberId, List<MultipartFile> images) {
        Member member = memberService.getById(memberId);

        // DTO를 엔티티로 변환
        Product product = Product.builder()
                .title(productDto.getTitle())
                .member(member)
                .conditions(productDto.getConditions())
                .categories(productDto.getCategories())
                .tradeTypes(productDto.getTradeTypes())
                .tradeLocation(productDto.getTradeLocation())
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
                .conditions(savedProduct.getConditions())
                .categories(savedProduct.getCategories())
                .tradeTypes(savedProduct.getTradeTypes())
                .tradeLocation(productDto.getTradeLocation())
                .initial_price(savedProduct.getInitial_price())
                .current_price(savedProduct.getCurrent_price())
                .minimum_price(savedProduct.getMinimum_price())
                .createdBy(savedProduct.getCreatedBy())
                .startTime(savedProduct.getStartTime())
                .likeCount(savedProduct.getLikeCount())
                .endTime(savedProduct.getEndTime())
                .details(savedProduct.getDetails())
                .imageUrls(savedProduct.getImageUrls())
                .build();

        return responseDto;
    }

    @Override
    public List<ProductListResponseDto> findAllProduct(Long memberId){
        List<Product> products = productRepository.findAll();

        List<Long> likedProductsIds = likeService.getLikeItems(memberId);

        List<ProductListResponseDto> collect = products.stream()
                .sorted(Comparator.comparing(Product::getProduct_id).reversed())
                .map(product -> {
                    String imageUrl = null;
                    if(!product.getImageList().isEmpty()){
                        imageUrl = product.getImageList().get(0).getAccessUrl();
                    }
                    ProductListResponseDto dto = ProductListResponseDto.builder()
                            .product_id(product.getProduct_id())
                            .title(product.getTitle())
                            .conditions(product.getConditions())
                            .initial_price(product.getInitial_price())
                            .categories(product.getCategories())
                            .tradeTypes(product.getTradeTypes())
                            .current_price(product.getCurrent_price())
                            .tradeLocation(product.getTradeLocation())
                            .createdBy(product.getCreatedBy())
                            .likeCount(product.getLikeCount())
                            .isSold(product.isSold())
                            .imageUrl(imageUrl)
                            .isLiked(likedProductsIds.contains(product.getProduct_id()))
                            .build();
                    return dto;
                })
                .collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<ProductListResponseDto> findAllByMemberId(Long memberId){
        List<Product> products = productRepository.findAllByMemberId(memberId);
        List<Long> likedProductsIds = likeService.getLikeItems(memberId);
        List<ProductListResponseDto> collect = products.stream()
                .map(product -> {
                    String imageUrl = null;
                    if(!product.getImageList().isEmpty()){
                        imageUrl = product.getImageList().get(0).getAccessUrl();
                    }
                    ProductListResponseDto dto = ProductListResponseDto.builder()
                            .product_id(product.getProduct_id())
                            .title(product.getTitle())
                            .conditions(product.getConditions())
                            .categories(product.getCategories())
                            .tradeTypes(product.getTradeTypes())
                            .initial_price(product.getInitial_price())
                            .current_price(product.getCurrent_price())
                            .tradeLocation(product.getTradeLocation())
                            .createdBy(product.getCreatedBy())
                            .likeCount(product.getLikeCount())
                            .isSold(product.isSold())
                            .imageUrl(imageUrl)
                            .isLiked(likedProductsIds.contains(product.getProduct_id()))
                            .build();
                    return dto;
                })
                .collect(Collectors.toList());
        return collect;
    }

    @Override
    public ProductResponseDto findProductById(Long memberId, Long product_id) {
        Optional<Product> product = productRepository.findById(product_id);
        boolean isLiked = likeService.isLiked(memberId, product_id);
        if (product.isPresent()) {
            Product findProduct = product.get();
            ProductResponseDto responseDto = ProductResponseDto.builder()
                    .product_id(findProduct.getProduct_id())
                    .title(findProduct.getTitle())
                    .conditions(findProduct.getConditions())
                    .categories(findProduct.getCategories())
                    .tradeTypes(findProduct.getTradeTypes())
                    .tradeLocation(findProduct.getTradeLocation())
                    .initial_price(findProduct.getInitial_price())
                    .current_price(findProduct.getCurrent_price())
                    .startTime(findProduct.getStartTime())
                    .endTime(findProduct.getEndTime())
                    .minimum_price(findProduct.getMinimum_price())
                    .createdBy(findProduct.getCreatedBy())
                    .details(findProduct.getDetails())
                    .likeCount(findProduct.getLikeCount())
                    .isSold(findProduct.isSold())
                    .isLiked(isLiked)
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

        List<String> existingImageUrls = product.getImageUrls();

        List<String> urlsToRetain = productDto.getImageUrlsToKeep();

        List<String> urlsToDelete = existingImageUrls.stream()
                .filter(url -> !urlsToRetain.contains(url))
                .collect(Collectors.toList());

        deleteExistingImages(product, urlsToDelete);

        List<Image> imageList = imageService.saveImages(images);
        product.getImageList().addAll(imageList);

        product.updateProduct(productDto.getTitle(), productDto.getCategories(), productDto.getTradeTypes(), productDto.getDetails(), productDto.getTradeLocation());

        ProductResponseDto productResponseDto = ProductResponseDto.builder()
                .product_id(product_id)
                .title(product.getTitle())
                .conditions(product.getConditions())
                .categories(product.getCategories())
                .tradeTypes(product.getTradeTypes())
                .tradeLocation(product.getTradeLocation())
                .initial_price(product.getInitial_price())
                .createdBy(product.getCreatedBy())
                .likeCount(product.getLikeCount())
                .details(product.getDetails())
                .imageUrls(product.getImageUrls())
                .build();

        return productResponseDto;
    }

    @Override
    public ProductResponseDto purchaseProductItem(Long product_id){
        Product product = productRepository.findById(product_id)
                .orElseThrow(() -> new IllegalArgumentException(product_id + "에 해당하는 물건이 없습니다."));

        if (product.isSold()){
            throw new RuntimeException("이미 판매된 물품입니다.");
        }

        product.setIsSold(true);
        ProductResponseDto productResponseDto = ProductResponseDto.builder()
                .product_id(product_id)
                .title(product.getTitle())
                .conditions(product.getConditions())
                .tradeTypes(product.getTradeTypes())
                .tradeLocation(product.getTradeLocation())
                .initial_price(product.getInitial_price())
                .details(product.getDetails())
                .likeCount(product.getLikeCount())
                .imageUrls(product.getImageUrls())
                .isSold(product.isSold())
                .build();

        productRepository.save(product);
        return productResponseDto;
    }

    @Override
    @Transactional
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

    //더미 10,000개로 확인 결과 성능에 영향은 없지만 추후 redis
    @Override
    @Scheduled(fixedRate = 60000)
    public void updateProductPrices() {
        LocalDateTime currentTime = LocalDateTime.now();
        int pageSize = 2000;
        int pageNumber = 0;

        Page<Product> page;
        do {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            page = productRepository.findActiveProduct(currentTime, pageable);

            List<Product> updatedProducts = new ArrayList<>();
            for (Product product : page.getContent()) {
                if (currentTime.isAfter(product.getUpdateTime().plusHours(1))) {
                    int newPrice = product.getCurrent_price() - (product.getInitial_price() / (int) product.getTotalHours());
                    if (newPrice < product.getMinimum_price()) {
                        newPrice = product.getMinimum_price();
                    }
                    product.updateCurrentPrice(newPrice);
                    product.updateTime(currentTime);
                    updatedProducts.add(product);
                }
            }
            saveUpdatedProducts(updatedProducts);
            pageNumber++;
        } while (page.hasNext());
        System.out.println("수행 완료");
    }

    //성능 최적화를 위해 트랜잭션을 db 수정이 있을 때만 적용
    //범위가 너무 넓으면 락이 오래 유지되어 성능 저하
    @Transactional
    public void saveUpdatedProducts(List<Product> updatedProducts) {
        if (!updatedProducts.isEmpty()) {
            productRepository.saveAll(updatedProducts);
        }
    }

    private void deleteExistingImages(Product product, List<String> urlsToDelete) {
        if (product.getImageUrls() != null) {
            for (String url : urlsToDelete) {
                Image imageToDelete = product.getImageList().stream()
                        .filter(image -> image.getAccessUrl().equals(url))
                        .findFirst()
                        .orElse(null);

                if (imageToDelete != null) {
                    imageService.deleteImage(imageToDelete.getStoredName());
                    product.getImageList().remove(imageToDelete);
                }
            }
        }
    }

    private Set<String> createRandomCategories() {
        Set<String> categories = new HashSet<>();
        categories.add("Electronics");
        categories.add("Fashion");
        categories.add("Home");
        categories.add("Toys");
        categories.add("Books");
        return categories;
    }

    private Set<String> createRandomTradeTypes() {
        Set<String> tradeTypes = new HashSet<>();
        tradeTypes.add("Sell");
        tradeTypes.add("Trade");
        return tradeTypes;
    }
}
