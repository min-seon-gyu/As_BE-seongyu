package Auction_shop.auction.domain.like.service;

import Auction_shop.auction.domain.like.Like;
import Auction_shop.auction.domain.like.repository.LikeRepository;
import Auction_shop.auction.domain.member.Member;
import Auction_shop.auction.domain.member.service.MemberService;
import Auction_shop.auction.domain.product.Product;
import Auction_shop.auction.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final ProductRepository productRepository;
    private final MemberService memberService;

    @Override
    public Like addProductToLike(Long memberId, Long productId) {
        Member member = memberService.getById(memberId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException(productId+"에 해당하는 물품이 없습니다."));

        if (likeRepository.findByMemberAndProduct(member, product) != null){
            throw new RuntimeException("이미 좋아요 되어있습니다.");
        }

        Like like = Like.builder()
                .member(member)
                .product(product)
                .build();

        member.addLike(like);
        product.addLike(like);

        return likeRepository.save(like);
    }

    @Override
    public List<Like> getLikeList(Long memberId) {
        Member member = memberService.getById(memberId);
        return likeRepository.findByMember(member);
    }

    @Override
    public void removeProductFromLike(Long memberId, Long productId) {
        Member member = memberService.getById(memberId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException(productId+"에 해당하는 물품이 없습니다."));

        Like like = likeRepository.findByMemberAndProduct(member, product);
        if(like != null){
            member.removeLike(like);
            product.removeLike(like);
            likeRepository.delete(like);
        }
    }

    public int getProductLikeCount(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException(productId+"에 해당하는 물품이 없습니다."));

        return product.getLikeCount();
    }
}
