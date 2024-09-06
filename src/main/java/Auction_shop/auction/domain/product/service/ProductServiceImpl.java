package Auction_shop.auction.domain.product.service;

import Auction_shop.auction.domain.image.Image;
import Auction_shop.auction.domain.image.service.ImageService;
import Auction_shop.auction.domain.member.Member;
import Auction_shop.auction.domain.member.repository.MemberRepository;
import Auction_shop.auction.domain.product.ProductDocument;
import Auction_shop.auction.domain.product.elasticRepository.ProductElasticsearchRepository;
import Auction_shop.auction.domain.product.repository.ProductJpaRepository;
import Auction_shop.auction.domain.product.Product;
import Auction_shop.auction.web.dto.product.*;
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
    private final ProductJpaRepository productJpaRepository;
    private final ProductElasticsearchRepository productElasticsearchRepository;
    private final MemberRepository memberRepository;
    private final ProductMapper productMapper;
    private final ImageService imageService;

    private final Random random = new Random();

    @Override
    @Transactional
    public Product save(ProductDto productDto, Long memberId, List<MultipartFile> images) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow();

        Product product = productMapper.toEntity(productDto, member);
        member.addProduct(product);

        List<Image> imageList = imageService.saveImages(images);
        product.setImageList(imageList);

        Product savedProduct = productJpaRepository.save(product);

        ProductDocument productDocument = productMapper.toDocument(savedProduct);
        productElasticsearchRepository.save(productDocument);

        return savedProduct;
    }

    @Override
    public Iterable<ProductDocument> findAllProduct(Long memberId){
        return productElasticsearchRepository.findAll();
    }

    @Override
    public Iterable<ProductDocument> findAllByNickname(String nickname){
        return productElasticsearchRepository.findByCreatedBy(nickname);
    }

    @Override
    public Iterable<ProductDocument> findByTitleLike(String title){
        return productElasticsearchRepository.findByTitleLike(title);
    }

    @Override
    public List<ProductDocument> getNewProducts() {
        List<ProductDocument> products = productElasticsearchRepository.findTop20ByOrderByCreatedAtDesc();
        int numberOfElementsToReturn = Math.min(products.size(), 5);
        return getRandomElements(products, numberOfElementsToReturn);
    }

    @Override
    public List<ProductDocument> getHotProducts(){
        List<ProductDocument> products = productElasticsearchRepository.findTop20ByOrderByLikeCountDesc();
        int numberOfElementsToReturn = Math.min(products.size(), 5);
        return getRandomElements(products, numberOfElementsToReturn);
    }

    @Override
    public Product findProductById(Long memberId, Long product_id) {
        Product product = productJpaRepository.findById(product_id)
                .orElseThrow(() -> new IllegalArgumentException(product_id + "에 해당하는 물건이 없습니다."));
        return product;
    }

    @Override
    @Transactional
    public Product updateProductById(ProductUpdateDto productUpdateDto, Long product_id, List<MultipartFile> images) {
        Product product = productJpaRepository.findById(product_id)
                .orElseThrow(() -> new IllegalArgumentException(product_id + "에 해당하는 물건이 없습니다."));

        List<String> existingImageUrls = product.getImageUrls();

        List<String> urlsToRetain = productUpdateDto.getImageUrlsToKeep();

        List<String> urlsToDelete = existingImageUrls.stream()
                .filter(url -> !urlsToRetain.contains(url))
                .collect(Collectors.toList());

        deleteExistingImages(product, urlsToDelete);

        List<Image> imageList = imageService.saveImages(images);
        product.getImageList().addAll(imageList);

        product.updateProduct(productUpdateDto.getTitle(), productUpdateDto.getCategories(), productUpdateDto.getDetails(), productUpdateDto.getTradeLocation(), productUpdateDto.getConditions());

        ProductDocument productDocument = productMapper.toDocument(product);
        productElasticsearchRepository.save(productDocument);

        return product;
    }

    @Override
    @Transactional
    public void updateCreateBy(String oldNickname, String newNickname, Long memberId){
        List<Product> products = productJpaRepository.findAllByMemberId(memberId);
        List<ProductDocument> productDocuments = new ArrayList<>();

        for (Product product : products) {
            product.updateCreatedBy(newNickname);
            ProductDocument productDocument = productMapper.toDocument(product);
            productDocuments.add(productDocument);
        }

        // Elasticsearch 배치 저장
        try {
            productElasticsearchRepository.saveAll(productDocuments);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void purchaseProductItem(Long product_id){
        Product product = productJpaRepository.findById(product_id)
                .orElseThrow(() -> new IllegalArgumentException(product_id + "에 해당하는 물건이 없습니다."));

        if (product.isSold()){
            throw new RuntimeException("이미 판매된 물품입니다.");
        }

        product.setIsSold(true);
        productJpaRepository.save(product);

        ProductDocument productDocument = productMapper.toDocument(product);
        productElasticsearchRepository.save(productDocument);
    }

    @Override
    @Transactional
    public boolean deleteProductById(Long product_id) {
        boolean isFound = productJpaRepository.existsById(product_id);
        Product product = productJpaRepository.findById(product_id)
                .orElseThrow(() -> new IllegalArgumentException(product_id + "에 해당하는 물건이 없습니다."));
        if (isFound) {
            for (Image image : product.getImageList()){
                imageService.deleteImage(image.getStoredName());
            }
            productJpaRepository.deleteById(product_id);
            productElasticsearchRepository.deleteById(product_id);
        }
        return isFound;
    }

    @Override
    public int findCurrentPriceById(Long productId){
        int price = productJpaRepository.findCurrentPriceById(productId);
        return price;
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
            page = productJpaRepository.findActiveProduct(currentTime, pageable);

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
            productJpaRepository.saveAll(updatedProducts);

            List<ProductDocument> productDocuments = updatedProducts.stream()
                    .map(productMapper::toDocument)
                    .collect(Collectors.toList());
            productElasticsearchRepository.saveAll(productDocuments);
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

    private List<ProductDocument> getRandomElements(List<ProductDocument> list, int number) {
        Random random = new Random();
        return random.ints(0, list.size())
                .distinct()
                .limit(number)
                .mapToObj(list::get)
                .collect(Collectors.toList());
    }

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

            productJpaRepository.save(product);
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
