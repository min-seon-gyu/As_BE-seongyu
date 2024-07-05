package Auction_shop.auction.domain.image.controller;

import Auction_shop.auction.domain.image.service.ImageService;
import Auction_shop.auction.web.dto.ImageSaveDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<List<String>> saveImage(@ModelAttribute ImageSaveDto imageSaveDto){
        List<String> collect = imageService.saveImages(imageSaveDto);
        return ResponseEntity.ok(collect);
    }
}
