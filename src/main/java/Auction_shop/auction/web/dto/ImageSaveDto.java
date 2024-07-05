package Auction_shop.auction.web.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageSaveDto {
    private List<MultipartFile> images = new ArrayList<>();
}
