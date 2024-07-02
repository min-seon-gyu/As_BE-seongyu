package Auction_shop.auction.product.validation;

import Auction_shop.auction.product.dto.ProductDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ProductValidator implements Validator {
    /**
     * 검증하려는 클래스 체크
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(ProductDto.class);
    }

    /**
     * 검증
     */
    @Override
    public void validate(Object target, Errors errors) {
        ProductDto productDto = (ProductDto) target;
        if (productDto.getSeller() == null) {
            errors.rejectValue("seller","seller.null","seller is null!");
        }
        if (productDto.getTitle() == null) {
            errors.rejectValue("title","title.null","title is null!");
        }
        if (productDto.getProduct_type() == null) {
            errors.rejectValue("product_type","product_type.null","product_type is null!");
        }
        if (productDto.getTrade() == null) {
            errors.rejectValue("trade","trade.null","trade is null!");
        }
        if (productDto.getInitial_price() <= 0) {
            errors.rejectValue("initial_price","initial_price.null","initial_price is null!");
        }
    }
}
