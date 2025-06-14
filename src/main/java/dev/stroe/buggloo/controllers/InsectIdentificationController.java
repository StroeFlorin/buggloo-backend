package dev.stroe.buggloo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import dev.stroe.buggloo.services.OpenAIService;

@RestController
@RequestMapping("/insect")
public class InsectIdentificationController {
    @Autowired
    private OpenAIService openAIService;

    @PostMapping(value = "/identify", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> identifyInsect(@RequestParam("image") MultipartFile imageFile) {
        try {
            byte[] imageBytes = imageFile.getBytes();
            String result = openAIService.identifyInsect(imageBytes);
            if (result == null) {
                return ResponseEntity.status(502).build();
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
