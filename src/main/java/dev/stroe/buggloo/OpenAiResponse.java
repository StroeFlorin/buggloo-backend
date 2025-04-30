package dev.stroe.buggloo;

import com.fasterxml.jackson.databind.ObjectMapper;

public class OpenAiResponse {
    public Choice[] choices;

    public InsectInfoDto toInsectInfoDto(ObjectMapper objectMapper) {
        if (choices != null && choices.length > 0 && choices[0].message != null && choices[0].message.content != null) {
            try {
                return objectMapper.readValue(choices[0].message.content, InsectInfoDto.class);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public static class Choice {
        public Message message;
    }

    public static class Message {
        public String content;
    }
}
