package com.moldi.allora.mapper;

import com.moldi.allora.entity.Image;
import com.moldi.allora.response.ImageResponse;
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
