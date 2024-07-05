package Auction_shop.auction.domain.image.service;

import Auction_shop.auction.domain.image.Image;
import Auction_shop.auction.domain.image.repository.ImageRepository;
import Auction_shop.auction.web.dto.ImageSaveDto;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageService {

    private final ImageRepository imageRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final AmazonS3Client amazonS3Client;

    @Transactional
    public List<String> saveImages(ImageSaveDto saveDto) {
        System.out.println(bucketName);
        List<String> resultList = new ArrayList<>();

        for(MultipartFile multipartFile : saveDto.getImages()) {
            String value = saveImage(multipartFile);
            resultList.add(value);
        }

        return resultList;
    }

    @Transactional
    public String saveImage(MultipartFile multipartFile){

        String originName = multipartFile.getOriginalFilename();
        Image image = new Image(originName);
        String filename = image.getStoredName();

        try{
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(multipartFile.getContentType());
            objectMetadata.setContentLength(multipartFile.getInputStream().available());

            amazonS3Client.putObject(bucketName, filename, multipartFile.getInputStream(), objectMetadata);

            String accessUrl = amazonS3Client.getUrl(bucketName, filename).toString();
            image.setAccessUrl(accessUrl);
        }catch (IOException e){
        }
        imageRepository.save(image);
        return image.getAccessUrl();
    }
}
