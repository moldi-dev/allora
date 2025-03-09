package com.moldi.allora.validation.validator;

import com.moldi.allora.validation.ImageList;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ImageListValidator implements ConstraintValidator<ImageList, List<MultipartFile>> {
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
    private static final String[] ALLOWED_TYPES = {
            "image/jpeg", "image/png"
    };
    private static long MAX_NUMBER_OF_IMAGES;

    @Override
    public void initialize(ImageList constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        MAX_NUMBER_OF_IMAGES = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(List<MultipartFile> images, ConstraintValidatorContext context) {
        if (images == null || images.isEmpty()) {
            setCustomMessage(context, "The image(s) is/are required");
            return false;
        }

        if (images.size() > MAX_NUMBER_OF_IMAGES) {
            setCustomMessage(context, "At most " + MAX_NUMBER_OF_IMAGES + " images can be uploaded");
            return false;
        }

        for (MultipartFile image : images) {
            if (image == null || image.isEmpty() || image.getSize() == 0) {
                setCustomMessage(context, "The image cannot be empty");
                return false;
            }

            if (image.getSize() > MAX_FILE_SIZE) {
                setCustomMessage(context, "The image size cannot exceed 5MB");
                return false;
            }

            String contentType = image.getContentType();

            if (contentType == null || !isAllowedContentType(contentType)) {
                setCustomMessage(context, "Only JPEG, JPG and PNG images are allowed.");
                return false;
            }
        }

        return true;
    }

    private boolean isAllowedContentType(String contentType) {
        for (String allowedType : ALLOWED_TYPES) {
            if (allowedType.equalsIgnoreCase(contentType)) {
                return true;
            }
        }
        return false;
    }

    private void setCustomMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
