package dev.stroe.buggloo.services;

import java.util.List;
import java.util.Base64;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletionContentPart;
import com.openai.models.chat.completions.ChatCompletionContentPartImage;
import com.openai.models.chat.completions.ChatCompletionContentPartText;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.openai.models.chat.completions.StructuredChatCompletionCreateParams;

import dev.stroe.buggloo.config.OpenAIConfig;
import dev.stroe.buggloo.exceptions.InsectIdentificationException;
import dev.stroe.buggloo.models.Insect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service for interacting with OpenAI API to identify insects.
 */
@Service
public class OpenAIService {

    private static final Logger logger = LoggerFactory.getLogger(OpenAIService.class);
    private static final String IDENTIFICATION_PROMPT = "Identify the insect in the image provided. If the organism in the image is not an insect, set is_insect to false and provide as much information as possible about what it actually is.";

    private final OpenAIClient client;
    private final OpenAIConfig config;

    public OpenAIService(OpenAIConfig config) {
        this.config = config;
        this.client = OpenAIOkHttpClient.builder()
                .apiKey(config.getApiKey())
                .build();
        logger.info("OpenAI service initialized.");
    }

    /**
     * Identifies an insect from the provided image bytes.
     * 
     * @param imageBytes the image data as byte array
     * @return Insect object with identification results
     * @throws InsectIdentificationException if identification fails
     */
    public Insect identifyInsect(byte[] imageBytes) {
        if (imageBytes == null || imageBytes.length == 0) {
            throw new InsectIdentificationException("Image data is empty or null");
        }

        try {
            logger.debug("Starting insect identification for image of size: {} bytes", imageBytes.length);

            String imageBase64Url = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imageBytes);

            ChatCompletionContentPart imageContentPart = createImageContentPart(imageBase64Url);
            ChatCompletionContentPart questionContentPart = createTextContentPart(IDENTIFICATION_PROMPT);

            StructuredChatCompletionCreateParams<Insect> createParams = buildChatCompletionParams(
                    List.of(questionContentPart, imageContentPart)
            );

            Insect result = client.chat().completions().create(createParams)
                    .choices()
                    .stream()
                    .flatMap(choice -> choice.message().content().stream())
                    .findFirst()
                    .orElse(null);

            if (result == null) {
                throw new InsectIdentificationException("No identification result received from OpenAI");
            }

            logger.debug("Successfully identified insect: {}", result.commonName);
            return result;

        } catch (Exception e) {
            logger.error("Failed to identify insect", e);
            throw new InsectIdentificationException("Failed to identify insect: " + e.getMessage(), e);
        }
    }

    private ChatCompletionContentPart createImageContentPart(String imageBase64Url) {
        return ChatCompletionContentPart.ofImageUrl(
                ChatCompletionContentPartImage.builder()
                        .imageUrl(ChatCompletionContentPartImage.ImageUrl.builder()
                                .url(imageBase64Url)
                                .build())
                        .build()
        );
    }

    private ChatCompletionContentPart createTextContentPart(String text) {
        return ChatCompletionContentPart.ofText(
                ChatCompletionContentPartText.builder()
                        .text(text)
                        .build()
        );
    }

    private StructuredChatCompletionCreateParams<Insect> buildChatCompletionParams(List<ChatCompletionContentPart> contentParts) {
        return ChatCompletionCreateParams.builder()
                .model(ChatModel.GPT_4O)
                .maxCompletionTokens(config.getMaxTokens())
                .responseFormat(Insect.class)
                .addUserMessageOfArrayOfContentParts(contentParts)
                .build();
    }
}