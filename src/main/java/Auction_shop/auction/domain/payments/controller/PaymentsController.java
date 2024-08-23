package Auction_shop.auction.domain.payments.controller;

import Auction_shop.auction.domain.payments.service.PaymentsService;
import Auction_shop.auction.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentsController {
    private final PaymentsService paymentsService;
    private final JwtUtil jwtUtil;

    @PostMapping("/{productId}/{impUid}")
    public ResponseEntity<String> createPayments(@RequestHeader("Authorization") String authorization,
                                                   @PathVariable Long productId,
                                                   @PathVariable String impUid){
        Long memberId = jwtUtil.extractMemberId(authorization);
        System.out.println("====================진입 성공=========================");

        try {
            String result = paymentsService.PaymentsVerify(impUid, productId, memberId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("결제 검증 중 오류 발생: " + e.getMessage());
        }
    }
}
