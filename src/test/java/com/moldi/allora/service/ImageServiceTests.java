package com.moldi.allora.service;

import com.moldi.allora.entity.Image;
import com.moldi.allora.exception.ResourceNotFoundException;
import com.moldi.allora.mapper.ImageMapper;
import com.moldi.allora.repository.ImageRepository;
import com.moldi.allora.response.ImageResponse;
import com.moldi.allora.service.implementation.ImageService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.MinioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTests {

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ImageMapper imageMapper;

    @Mock
    private MinioClient minioClient;

    @Mock
    private MultipartFile file;

    @InjectMocks
    private ImageService imageService;

    private Image image;
    private ImageResponse imageResponse;
    private InputStream mockInputStream;

    @BeforeEach
    void setUp() throws IllegalAccessException, NoSuchFieldException {
        image = new Image();
        image.setImageId(1L);
        image.setName("test.jpg");
        image.setObjectName("test.jpg");
        image.setSize(BigDecimal.valueOf(1.23));
        image.setType("image/jpeg");
        image.setUrl("http://minio-url/bucket/test.jpg");

        imageResponse = new ImageResponse(1L, "test.jpg", BigDecimal.valueOf(1.23), "image/jpeg", "http://minio-url/bucket/test.jpg");

        Field bucketNameField = ImageService.class.getDeclaredField("bucketName");
        bucketNameField.setAccessible(true);
        bucketNameField.set(imageService, "test-bucket");

        byte[] mockFileContent = "Mock file content".getBytes();
        mockInputStream = new ByteArrayInputStream(mockFileContent);
    }

    @Test
    void findById_ShouldReturnImageResponse_WhenImageExists() {
        when(imageRepository.findById(1L)).thenReturn(Optional.of(image));
        when(imageMapper.toImageResponse(image)).thenReturn(imageResponse);

        ImageResponse result = imageService.findById(1L);

        assertNotNull(result);
        assertEquals(imageResponse.imageId(), result.imageId());
        assertEquals(imageResponse.name(), result.name());
        assertEquals(imageResponse.size(), result.size());
        assertEquals(imageResponse.type(), result.type());
        assertEquals(imageResponse.url(), result.url());

        verify(imageRepository, times(1)).findById(1L);
        verify(imageMapper, times(1)).toImageResponse(image);
    }

    @Test
    void findById_ShouldThrowResourceNotFoundException_WhenImageDoesNotExist() {
        when(imageRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> imageService.findById(1L));

        verify(imageRepository, times(1)).findById(1L);
        verify(imageMapper, never()).toImageResponse(any());
    }

    @Test
    void save_ShouldReturnImageResponse_WhenFileIsSavedSuccessfully() throws MinioException, IOException, InvalidKeyException, NoSuchAlgorithmException {
        when(file.getOriginalFilename()).thenReturn("test.jpg");
        when(file.getSize()).thenReturn(123456L);
        when(file.getContentType()).thenReturn("image/jpeg");
        when(file.getInputStream()).thenReturn(mockInputStream);

        when(imageRepository.save(any(Image.class))).thenReturn(image);
        when(imageMapper.toImageResponse(image)).thenReturn(imageResponse);

        ImageResponse result = imageService.save(file);

        assertNotNull(result);
        assertEquals(imageResponse.imageId(), result.imageId());
        assertEquals(imageResponse.name(), result.name());
        assertEquals(imageResponse.size(), result.size());
        assertEquals(imageResponse.type(), result.type());
        assertEquals(imageResponse.url(), result.url());

        verify(minioClient, times(1)).putObject(any(PutObjectArgs.class));
        verify(imageRepository, times(1)).save(any(Image.class));
        verify(imageMapper, times(1)).toImageResponse(image);
    }

    @Test
    void save_ShouldThrowRuntimeException_WhenFileSavingFails() throws MinioException, IOException, InvalidKeyException, NoSuchAlgorithmException {
        when(file.getOriginalFilename()).thenReturn("test.jpg");
        when(file.getSize()).thenReturn(123456L);
        when(file.getContentType()).thenReturn("image/jpeg");
        when(file.getInputStream()).thenReturn(mockInputStream);

        doThrow(new RuntimeException(new MinioException("Error saving file")))
                .when(minioClient).putObject(any(PutObjectArgs.class));

        assertThrows(RuntimeException.class, () -> imageService.save(file));

        verify(minioClient, times(1)).putObject(any(PutObjectArgs.class));
        verify(imageRepository, never()).save(any(Image.class));
        verify(imageMapper, never()).toImageResponse(any());
    }

    @Test
    void delete_ShouldDeleteImage_WhenImageExists() throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        doNothing().when(minioClient).removeObject(any(RemoveObjectArgs.class));
        doNothing().when(imageRepository).delete(image);

        imageService.delete(image);

        verify(minioClient, times(1)).removeObject(any(RemoveObjectArgs.class));
        verify(imageRepository, times(1)).delete(image);
    }

    @Test
    void delete_ShouldThrowRuntimeException_WhenFileDeletionFails() throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        doThrow(new RuntimeException(new MinioException("Error deleting file")))
                .when(minioClient).removeObject(any(RemoveObjectArgs.class));

        assertThrows(RuntimeException.class, () -> imageService.delete(image));

        verify(minioClient, times(1)).removeObject(any(RemoveObjectArgs.class));
        verify(imageRepository, never()).delete(any(Image.class));
    }
}