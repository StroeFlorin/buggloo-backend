package dev.stroe.buggloo.controllers;

import dev.stroe.buggloo.models.ApiResponse;
import dev.stroe.buggloo.models.Insect;
import dev.stroe.buggloo.services.ImageValidationService;
import dev.stroe.buggloo.services.OpenAIService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * REST controller for insect identification endpoints.
 */
@RestController
@RequestMapping("/insect")
public class InsectIdentificationController {

    private static final Logger logger = LoggerFactory.getLogger(InsectIdentificationController.class);

    private final OpenAIService openAIService;
    private final ImageValidationService imageValidationService;

    public InsectIdentificationController(OpenAIService openAIService, ImageValidationService imageValidationService) {
        this.openAIService = openAIService;
        this.imageValidationService = imageValidationService;
    }

    /**
     * Identifies an insect from an uploaded image.
     * 
     * @param imageFile the uploaded image file
     * @return ResponseEntity containing the identification results
     */
    @PostMapping(value = "/identify")
    public ResponseEntity<ApiResponse<Insect>> identifyInsect(@RequestParam("image") MultipartFile imageFile) throws Exception {
        logger.info("Received insect identification request for file: {}", imageFile.getOriginalFilename());

        try {
            // Validate the uploaded image
            imageValidationService.validateImage(imageFile);

            // Convert to bytes and identify
            byte[] imageBytes = imageFile.getBytes();
            Insect result = openAIService.identifyInsect(imageBytes);

            logger.info("Successfully identified insect: {} (is_insect: {})", 
                    result.commonName, result.isInsect);

            return ResponseEntity.ok(ApiResponse.success(result, "Insect identification completed successfully"));
        } catch (Exception e) {
            logger.error("Error processing insect identification request", e);
            // Re-throw to let GlobalExceptionHandler handle it
            throw e;
        }
    }

    /**
     * Health check endpoint.
     * 
     * @return ResponseEntity indicating service status
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(ApiResponse.success("Service is running", "Insect identification service is healthy"));
    }
}
