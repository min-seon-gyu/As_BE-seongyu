package Auction_shop.auction.security.jwt;

import Auction_shop.auction.domain.member.Address;
import Auction_shop.auction.web.dto.member.MemberResponseDto;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
@Component
public class JwtUtil {

    private final SecretKey secretKey;

    public JwtUtil(@Value("${spring.jwt.secret}") String secretKey) {
        this.secretKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public Long getId(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("id", Long.class);
    }

    public String getUsername(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
    }

    public String getName(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("name", String.class);
    }

    public Address getAddress(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("address", Address.class);
    }

    public String getPhone(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("phone", String.class);
    }

    public Long getPoint(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("point", Long.class);
    }

    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public String getCategory(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category", String.class);
    }

    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public MemberResponseDto getMemberResponseDto(String token){
        return MemberResponseDto.builder()
                .id(getId(token))
                .username(getUsername(token))
                .name(getName(token))
                .address(getAddress(token))
                .phone(getPhone(token))
                .point(getPoint(token))
                .role(getRole(token))
                .build();
    }

    public String createJwt(String category, MemberResponseDto memberResponseDto, Long expiredMs) {
        return Jwts.builder()
                .claim("id", memberResponseDto.getId())
                .claim("username", memberResponseDto.getUsername())
                .claim("name", memberResponseDto.getName())
                .claim("address", memberResponseDto.getAddress())
                .claim("phone", memberResponseDto.getPhone())
                .claim("point", memberResponseDto.getPoint())
                .claim("role", memberResponseDto.getRole())
                .claim("category", category)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    public Long extractMemberId(String authorization) {
        try {
            String token = extractToken(authorization);
            return getId(token);
        } catch (Exception e) {
            throw new IllegalArgumentException("유효하지 않은 토큰: " + e.getMessage());
        }
    }

    private String extractToken(String authorization) {
        if (authorization != null && authorization.startsWith("Bearer ")) {
            return authorization.replace("Bearer ", "").trim(); // 공백 제거
        }
        throw new IllegalArgumentException("헤더가 유효하지 않습니다.");
    }
}
