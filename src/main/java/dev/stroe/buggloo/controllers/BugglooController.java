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
 * REST controller for Buggloo application endpoints.
 */
@RestController
@RequestMapping("/insect")
public class BugglooController {

    private static final Logger logger = LoggerFactory.getLogger(BugglooController.class);

    private final OpenAIService openAIService;
    private final ImageValidationService imageValidationService;

    public BugglooController(OpenAIService openAIService, ImageValidationService imageValidationService) {
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
     * Generates a chat response about insects using OpenAI API.
     * 
     * @param pastConversation the previous conversation context (optional)
     * @param message the current user message
     * @param insectName the name of the insect being discussed (optional)
     * @return ResponseEntity containing the chat response
     */
    @PostMapping(value = "/chat")
    public ResponseEntity<ApiResponse<String>> chat(
            @RequestParam(value = "pastConversation", required = false) String pastConversation,
            @RequestParam("message") String message,
            @RequestParam(value = "insectName", required = false) String insectName) throws Exception {
        
        logger.info("Received chat request - insectName: {}, message: {}", insectName, message);

        try {
            String response = openAIService.generateChatResponse(pastConversation, message, insectName);

            logger.info("Successfully generated chat response of length: {}", response.length());

            return ResponseEntity.ok(ApiResponse.success(response, "Chat response generated successfully"));
        } catch (Exception e) {
            logger.error("Error processing chat request", e);
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
        return ResponseEntity.ok(ApiResponse.success("Service is running", "Buggloo service is healthy"));
    }
} 