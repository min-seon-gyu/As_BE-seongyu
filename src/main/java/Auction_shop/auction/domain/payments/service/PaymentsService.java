package Auction_shop.auction.domain.payments.service;

import Auction_shop.auction.domain.member.Member;
import Auction_shop.auction.domain.member.service.MemberService;
import Auction_shop.auction.domain.payments.Payments;
import Auction_shop.auction.domain.payments.repository.PaymentsRepository;
import Auction_shop.auction.domain.product.repository.ProductRepository;
import Auction_shop.auction.domain.product.service.ProductService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PaymentsService {

    private IamportClient iamportClient;

    private final PaymentsRepository paymentsRepository;
    private final MemberService memberService;
    private final ProductService productService;

    @Value("${iamport.key}")
    private String apiKey;

    @Value("${iamport.secret}")
    private String secretKey;

    @PostConstruct
    public void PaymentsService(){
        this.iamportClient = new IamportClient(apiKey, secretKey);
    }

    @Transactional
    public String PaymentsVerify(String impUid, Long productId, Long memberId) throws IamportResponseException, IOException {

        System.out.println("impUid = " + impUid);
        System.out.println("productId = " + productId);
        System.out.println("memberId = " + memberId);

        IamportResponse<Payment> iamportResponse;
        try {
            iamportResponse = iamportClient.paymentByImpUid(impUid);
        } catch (Exception e) {
            System.out.println("결제 정보 조회 실패");
            throw new RuntimeException("결제 정보를 조회하는 데 실패: " + e.getMessage());
        }

        String merchantUid = iamportResponse.getResponse().getMerchantUid();

        int paidAmount = iamportResponse.getResponse().getAmount().intValue();
        System.out.println("paidAmount = " + paidAmount);
        int productPrice = productService.findCurrentPriceById(productId);
        System.out.println("productPrice = " + productPrice);

        if (paidAmount != productPrice) {
            cancelAllPayment(impUid);
            return "지불한 금액 : " + paidAmount + "와 데이터베이스 금액: " + productPrice + "가 일치하지 않음.";
        }

        Member member = memberService.getById(memberId);

        Payments payments = Payments.builder()
                .paymentMethod(iamportResponse.getResponse().getPayMethod())
                .amount(paidAmount)
                .merchantUid(merchantUid)
                .member(member)
                .build();

        paymentsRepository.save(payments);

        productService.purchaseProductItem(productId);

        return "결제가 완료되었습니다.";
    }

    private void cancelAllPayment(String impUid) {
        try {
            CancelData cancelData = new CancelData(impUid, true);
            cancelData.setReason("결제 금액 불일치로 인한 취소");

            iamportClient.cancelPaymentByImpUid(cancelData);
            System.out.println("결제가 취소되었습니다: " + impUid);

        } catch (Exception e) {
            throw new RuntimeException("결제 취소 실패: " + e.getMessage());
        }
    }
}
