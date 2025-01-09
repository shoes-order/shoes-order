package com.personal.domain.review.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.personal.common.code.ResponseCode;
import com.personal.common.component.FileManagement;
import com.personal.common.dto.FileUploadDto;
import com.personal.common.exception.custom.BadRequestException;
import com.personal.domain.review.repository.ReviewImageRepository;
import com.personal.entity.review.Review;
import com.personal.entity.review.ReviewImage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewImageService {

    private final FileManagement fileManagement;
    private final AmazonS3Client amazonS3Client;

    private final ReviewImageRepository reviewImageRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public void save(Review review , List<MultipartFile> files) {
        try {
            for (MultipartFile file : files) {
                // 리뷰 이미지 처리
                awsS3Upload(file).thenAccept(url -> {
                    // 리뷰 이미지 등록
                    ReviewImage reviewImage = new ReviewImage(url , review);
                    reviewImageRepository.save(reviewImage);
                });
            }
        } catch (IOException e) {
            throw new BadRequestException(ResponseCode.INVALID_IMAGE_ACCESS);
        }
    }

    @Transactional
    public void update(Review review , List<Long> imageNumber , List<MultipartFile> files) {
        try {
            for (int i = 0; i < imageNumber.size(); i++) {
                Long imageId = imageNumber.get(i);
                MultipartFile file = files.get(i);
                reviewImageRepository.deleteById(imageId);

                awsS3Upload(file).thenAccept(url -> {
                    // 리뷰 이미지 등록
                    ReviewImage reviewImage = new ReviewImage(url , review);
                    reviewImageRepository.save(reviewImage);
                });
            }
        } catch (IOException e) {
            throw new BadRequestException(ResponseCode.INVALID_IMAGE_ACCESS);
        }
    }

    @Transactional
    public void remove(Review review) {
        List<ReviewImage> reviewImages = reviewImageRepository.findByReview(review);

        for (ReviewImage reviewImage : reviewImages) {
            String filename = fileManagement.extractFilenameFromUrl(reviewImage.getImageUrl());
            amazonS3Client.deleteObject(bucket, filename);
            reviewImageRepository.delete(reviewImage);
        }
    }

    @Async
    protected CompletableFuture<String> awsS3Upload(MultipartFile file) throws IOException {
        FileUploadDto fileUploadDto = fileManagement.storeFile(file);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(fileUploadDto.getFileSize());
        metadata.setContentType(fileUploadDto.getContentType());
        amazonS3Client.putObject(bucket, fileUploadDto.getConvertFileName(), file.getInputStream(), metadata);
        return CompletableFuture.completedFuture(amazonS3Client.getUrl(bucket, fileUploadDto.getConvertFileName()).toString());
    }
}
