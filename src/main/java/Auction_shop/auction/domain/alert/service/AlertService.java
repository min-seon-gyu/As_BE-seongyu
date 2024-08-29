package Auction_shop.auction.domain.alert.service;

import Auction_shop.auction.domain.alert.Alert;
import Auction_shop.auction.domain.alert.repository.AlertRepository;
import Auction_shop.auction.web.dto.alert.AlertCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AlertService {
    private final AlertRepository alertRepository;
    public List<Alert> getList(Long memberId) {
        return alertRepository.findByMemberId(memberId);
    }

    @Transactional
    public Long add(AlertCreateDto alertCreateDto){
        Alert alert = Alert.builder()
                .memberId(alertCreateDto.getMemberId())
                .content(alertCreateDto.getContent())
                .build();

        alertRepository.save(alert);

        return alert.getId();
    }

    @Transactional
    public void delete(Long id) {
        alertRepository.deleteById(id);
    }
}
