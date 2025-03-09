package com.moldi.allora.service;

import com.moldi.allora.entity.Image;
import com.moldi.allora.response.ImageResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface IImageService {
    ImageResponse findById(Long imageId);
    ImageResponse save(MultipartFile file);
    void delete(Image image);
}
