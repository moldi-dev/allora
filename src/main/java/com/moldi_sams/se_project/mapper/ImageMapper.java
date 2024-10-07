package com.moldi_sams.se_project.mapper;

import com.moldi_sams.se_project.entity.Image;
import com.moldi_sams.se_project.response.ImageResponse;
import org.springframework.stereotype.Service;

@Service
public class ImageMapper {
    public ImageResponse toImageResponse(Image image) {
        return new ImageResponse(
                image.getImageId(),
                image.getName(),
                image.getSize(),
                image.getType(),
                image.getUrl()
        );
    }

    public Image toImage(ImageResponse imageResponse) {
        return Image
                .builder()
                .imageId(imageResponse.imageId())
                .name(imageResponse.name())
                .size(imageResponse.size())
                .type(imageResponse.type())
                .url(imageResponse.url())
                .build();
    }
}
