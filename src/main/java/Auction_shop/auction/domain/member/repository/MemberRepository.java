package Auction_shop.auction.domain.member.repository;

import Auction_shop.auction.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUuid(String uuid);

    boolean existsByName(String name);
}
