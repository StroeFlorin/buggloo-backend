package dev.stroe.buggloo.services;

import java.util.List;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletionContentPart;
import com.openai.models.chat.completions.ChatCompletionContentPartImage;
import com.openai.models.chat.completions.ChatCompletionContentPartText;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.openai.models.chat.completions.StructuredChatCompletionCreateParams;
import java.util.Base64;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.stroe.buggloo.models.Insect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OpenAIService {
    @Value("${openai.api.key}")
    private String apikey;

    public String identifyInsect(byte[] imageBytes) {
        OpenAIClient client = OpenAIOkHttpClient.builder()
                .apiKey(this.apikey)
                .build();

        String imageBase64Url = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imageBytes);

        ChatCompletionContentPart imageContentPart =
                ChatCompletionContentPart.ofImageUrl(ChatCompletionContentPartImage.builder()
                        .imageUrl(ChatCompletionContentPartImage.ImageUrl.builder()
                                .url(imageBase64Url)
                                .build())
                        .build());

        ChatCompletionContentPart questionContentPart =
                ChatCompletionContentPart.ofText(ChatCompletionContentPartText.builder()
                        .text("Identify the insect in the image provided.")
                        .build());

        StructuredChatCompletionCreateParams<Insect> createParams = ChatCompletionCreateParams.builder()
                .model(ChatModel.GPT_4_1)
                .maxCompletionTokens(2048)
                .responseFormat(Insect.class)
                .addUserMessageOfArrayOfContentParts(List.of(questionContentPart, imageContentPart))
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        return client.chat().completions().create(createParams).choices().stream()
                .flatMap(choice -> choice.message().content().stream())
                .findFirst()
                .map(insect -> {
                    try {
                        return objectMapper.writeValueAsString(insect);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .orElse(null);
    }
}