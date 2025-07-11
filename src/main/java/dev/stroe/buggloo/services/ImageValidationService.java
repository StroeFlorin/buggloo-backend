package dev.stroe.buggloo.services;

import dev.stroe.buggloo.exceptions.InvalidImageException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

/**
 * Service for validating uploaded images.
 */
@Service
public class ImageValidationService {

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg", 
            "image/jpg", 
            "image/png", 
            "image/gif", 
            "image/bmp",
            "image/webp"
    );

    /**
     * Validates the uploaded image file.
     * 
     * @param file the uploaded file
     * @throws InvalidImageException if the file is invalid
     */
    public void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidImageException("No image file provided");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new InvalidImageException("Invalid image format. Supported formats: JPEG, PNG, GIF, BMP, WebP");
        }

        String filename = file.getOriginalFilename();
        if (filename == null || filename.trim().isEmpty()) {
            throw new InvalidImageException("Invalid filename");
        }
    }
} 