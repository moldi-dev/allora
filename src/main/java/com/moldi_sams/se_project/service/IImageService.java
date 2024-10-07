package com.moldi_sams.se_project.service;

import com.moldi_sams.se_project.entity.Image;
import com.moldi_sams.se_project.response.ImageResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface IImageService {
    ImageResponse findById(Long imageId);
    ImageResponse save(MultipartFile file);
    void delete(Image image);
}
