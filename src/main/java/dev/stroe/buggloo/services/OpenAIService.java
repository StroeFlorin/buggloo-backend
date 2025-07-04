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
import dev.stroe.buggloo.exceptions.NoInsectException;
import dev.stroe.buggloo.exceptions.ServiceException;
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
     * @throws ServiceException if identification fails
     */
    public Insect identifyInsect(byte[] imageBytes) {
        try {
            logger.debug("Starting insect identification for image of size: {} bytes", imageBytes.length);

            String imageBase64Url = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imageBytes);

            // Create image content part
            ChatCompletionContentPart imageContentPart = ChatCompletionContentPart.ofImageUrl(
                    ChatCompletionContentPartImage.builder()
                            .imageUrl(ChatCompletionContentPartImage.ImageUrl.builder()
                                    .url(imageBase64Url)
                                    .build())
                            .build()
            );

            // Create text content part
            ChatCompletionContentPart textContentPart = ChatCompletionContentPart.ofText(
                    ChatCompletionContentPartText.builder()
                            .text(IDENTIFICATION_PROMPT)
                            .build()
            );

            // Build chat completion parameters
            StructuredChatCompletionCreateParams<Insect> createParams = ChatCompletionCreateParams.builder()
                    .model(ChatModel.GPT_4_1)
                    .maxCompletionTokens(config.getMaxTokens())
                    .responseFormat(Insect.class)
                    .addUserMessageOfArrayOfContentParts(List.of(textContentPart, imageContentPart))
                    .build();

            Insect result = client.chat().completions().create(createParams)
                    .choices()
                    .stream()
                    .flatMap(choice -> choice.message().content().stream())
                    .findFirst()
                    .orElse(null);

            if (result == null) {
                throw new ServiceException("No identification result received from OpenAI");
            }

            if (!result.isInsect) {
                throw new NoInsectException("No insect identified");
            }

            return result;

        } catch (NoInsectException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Failed to identify insect", e);
            throw new ServiceException("Failed to identify insect: " + e.getMessage(), e);
        }
    }

    /**
     * Generates a chat response using OpenAI API based on past conversation, current message, and insect context.
     * 
     * @param pastConversation the previous conversation context
     * @param message the current user message
     * @param insectName the name of the insect being discussed
     * @return String containing the AI's response
     * @throws ServiceException if chat generation fails
     */
    public String generateChatResponse(String pastConversation, String message, String insectName) {
        if (message == null || message.trim().isEmpty()) {
            throw new ServiceException("Message cannot be empty");
        }

        try {
            logger.debug("Generating chat response for insect: {}, message: {}", insectName, message);

            // Build developer message with system context
            StringBuilder developerMessageBuilder = new StringBuilder();
            
            // Add system context about the insect
            String systemContext = String.format(
                "You are an expert insect assistant. You are discussing %s. " +
                "ONLY respond to questions and conversations about insects, bugs, and related entomology topics. " +
                "If the user asks about anything unrelated to insects, politely redirect them back to insect topics. " +
                "Provide concise, accurate information about insects only. " +
                "Keep responses brief, conversational, and in a single paragraph without line breaks or newlines. " +
                "Avoid using \\n or multiple paragraphs in your response.",
                insectName != null && !insectName.trim().isEmpty() ? insectName : "insects in general"
            );
            developerMessageBuilder.append(systemContext);
            
            // Add past conversation context if provided
            if (pastConversation != null && !pastConversation.trim().isEmpty()) {
                developerMessageBuilder.append(" Previous conversation context: ");
                developerMessageBuilder.append(pastConversation);
            }

            ChatCompletionCreateParams createParams = ChatCompletionCreateParams.builder()
                    .model(ChatModel.GPT_4_1)
                    .maxCompletionTokens(2048)
                    .addDeveloperMessage(developerMessageBuilder.toString())
                    .addUserMessage(message)
                    .build();

            String result = client.chat().completions().create(createParams)
                    .choices()
                    .stream()
                    .findFirst()
                    .map(choice -> choice.message().content().orElse(""))
                    .orElse("");

            if (result.isEmpty()) {
                throw new ServiceException("No response received from OpenAI");
            }

            return result;

        } catch (Exception e) {
            logger.error("Failed to generate chat response", e);
            throw new ServiceException("Failed to generate chat response: " + e.getMessage(), e);
        }
    }
}