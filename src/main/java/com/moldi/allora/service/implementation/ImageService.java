package com.moldi.allora.service.implementation;

import com.moldi.allora.entity.Image;
import com.moldi.allora.exception.ResourceNotFoundException;
import com.moldi.allora.mapper.ImageMapper;
import com.moldi.allora.repository.ImageRepository;
import com.moldi.allora.response.ImageResponse;
import com.moldi.allora.service.IImageService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.MinioException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageService implements IImageService {
    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;
    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Value("${minio.url}")
    private String minioUrl;

    @Override
    public ImageResponse findById(Long imageId) {
        Image searchedImageById = imageRepository
                .findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("The image by the provided id could not be found"));

        return imageMapper.toImageResponse(searchedImageById);
    }

    @Override
    public ImageResponse save(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String objectFileName = file.getOriginalFilename() + "_" + LocalDateTime.now();

        Image newImage = Image.builder()
                .size(BigDecimal.valueOf(file.getSize()).divide(BigDecimal.valueOf(1024 * 1024), 2, RoundingMode.HALF_UP))
                .type(file.getContentType())
                .name(fileName)
                .objectName(objectFileName)
                .build();

        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectFileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            String fileUrl = String.format("%s/%s/%s", minioUrl, bucketName, objectFileName);
            newImage.setUrl(fileUrl);

            return imageMapper.toImageResponse(imageRepository.save(newImage));
        }

        catch (MinioException | IOException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Error saving file to MinIO: ", e);
        }
    }

    @Override
    public void delete(Image image) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(image.getObjectName())
                            .build()
            );
        }

        catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Error deleting file from MinIO: ", e);
        }

        imageRepository.delete(image);
    }
}
