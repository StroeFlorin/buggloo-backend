package dev.stroe.buggloo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Base64;

@RestController
@RequestMapping("/insect")
public class InsectIdentificationController {
    @Autowired
    private OpenAiService openAiService;

    @PostMapping(value = "/identify", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<InsectInfoDto> identifyInsect(@RequestParam("image") MultipartFile imageFile) {
        try {
            byte[] imageBytes = imageFile.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            InsectInfoDto result = openAiService.identifyInsect(base64Image);
            if (result == null) {
                return ResponseEntity.status(502).build();
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
