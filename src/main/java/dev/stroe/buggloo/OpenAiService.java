package dev.stroe.buggloo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class OpenAiService {
    @Value("${openai.api.key}")
    private String openAiApiKey;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    public InsectInfoDto identifyInsect(String base64Image) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiApiKey);

        Map<String, Object> systemContent = new HashMap<>();
        systemContent.put("type", "text");
        systemContent.put("text", "Identify the insect in the image and respond with information about that insect.");

        Map<String, Object> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", Collections.singletonList(systemContent));

        Map<String, Object> userContent = new HashMap<>();
        userContent.put("type", "image_url");
        userContent.put("image_url", Map.of("url", "data:image/jpeg;base64," + base64Image));
        Map<String, Object> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", Collections.singletonList(userContent));

        List<Object> messages = new ArrayList<>();
        messages.add(systemMessage);
        messages.add(userMessage);

        Map<String, Object> jsonSchema = new HashMap<>();
        jsonSchema.put("name", "insect_info");
        jsonSchema.put("strict", true);
        jsonSchema.put("schema", getInsectInfoSchema());

        Map<String, Object> responseFormat = new HashMap<>();
        responseFormat.put("type", "json_schema");
        responseFormat.put("json_schema", jsonSchema);

        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-4.1");
        body.put("messages", messages);
        body.put("response_format", responseFormat);
        body.put("temperature", 1);
        body.put("max_completion_tokens", 2048);
        body.put("top_p", 1);
        body.put("frequency_penalty", 0);
        body.put("presence_penalty", 0);
        body.put("store", false);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        // Get raw response as String
        ResponseEntity<String> rawResponse = restTemplate.exchange(
                OPENAI_API_URL,
                HttpMethod.POST,
                request,
                String.class
        );

        // Map raw response to OpenAiResponse
        OpenAiResponse responseBody = null;
        try {
            responseBody = objectMapper.readValue(rawResponse.getBody(), OpenAiResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (responseBody != null) {
            return responseBody.toInsectInfoDto(objectMapper);
        }
        return null;
    }

    private Map<String, Object> getInsectInfoSchema() {
        Map<String, Object> schema = new HashMap<>();
        schema.put("type", "object");
        Map<String, Object> properties = new HashMap<>();
        properties.put("common_name", Map.of("type", "string", "description", "The common name of the insect."));
        properties.put("scientific_name", Map.of("type", "string", "description", "The scientific name of the insect."));
        properties.put("alternative_names", Map.of("type", "array", "description", "List of alternative names for the insect.", "items", Map.of("type", "string")));
        properties.put("domain", Map.of("type", "string", "description", "The domain classification of the insect."));
        properties.put("kingdom", Map.of("type", "string", "description", "The kingdom classification of the insect."));
        properties.put("phylum", Map.of("type", "string", "description", "The phylum classification of the insect."));
        properties.put("class", Map.of("type", "string", "description", "The class classification of the insect."));
        properties.put("order", Map.of("type", "string", "description", "The order classification of the insect."));
        properties.put("family", Map.of("type", "string", "description", "The family classification of the insect."));
        properties.put("genus", Map.of("type", "string", "description", "The genus classification of the insect."));
        properties.put("species", Map.of("type", "string", "description", "The species classification of the insect."));
        properties.put("geographic_range", Map.of("type", "string", "description", "The geographic range of the insect. (continents, countries, specific regions, etc.)"));
        properties.put("habitat_type", Map.of("type", "string", "description", "The habitat type where the insect is found. (forest, desert, freshwater, urban, etc.)"));
        properties.put("seasonal_appearance", Map.of("type", "string", "description", "The months or seasons when the insect is active."));
        properties.put("size", Map.of("type", "string", "description", "Size description of the insect."));
        properties.put("colors", Map.of("type", "array", "description", "An array of colors representing the insect's coloration.", "items", Map.of("type", "string")));
        properties.put("has_wings", Map.of("type", "boolean", "description", "Indicates if the insect has wings."));
        properties.put("leg_count", Map.of("type", "integer", "description", "Number of legs the insect has."));
        properties.put("distinctive_markings", Map.of("type", "string", "description", "Description of distinctive markings on the insect."));
        properties.put("diet", Map.of("type", "string", "description", "The dietary classification of the insect."));
        properties.put("activity_time", Map.of("type", "string", "description", "The time of day the insect is most active."));
        properties.put("lifespan", Map.of("type", "string", "description", "The typical lifespan of the insect."));
        properties.put("predators", Map.of("type", "array", "description", "List of predators that feed on this insect.", "items", Map.of("type", "string")));
        properties.put("defense_mechanisms", Map.of("type", "array", "description", "Methods the insect uses to defend itself.", "items", Map.of("type", "string")));
        properties.put("role_in_ecosystem", Map.of("type", "string", "description", "The role the insect plays in its ecosystem."));
        properties.put("interesting_facts", Map.of("type", "array", "description", "A collection of interesting facts about the insect.", "items", Map.of("type", "string")));
        properties.put("similar_species", Map.of("type", "array", "description", "List of species that are similar to this insect.", "items", Map.of("type", "string")));
        properties.put("conservation_status", Map.of("type", "string", "description", "The conservation status of the insect."));
        properties.put("url_wikipedia", Map.of("type", "string", "description", "A URL to the Wikipedia page of the insect."));
        schema.put("properties", properties);
        schema.put("required", List.of(
                "common_name", "scientific_name", "alternative_names", "domain", "kingdom", "phylum", "class", "order", "family", "genus", "species", "geographic_range", "habitat_type", "seasonal_appearance", "size", "colors", "has_wings", "leg_count", "distinctive_markings", "diet", "activity_time", "lifespan", "predators", "defense_mechanisms", "role_in_ecosystem", "interesting_facts", "similar_species", "conservation_status", "url_wikipedia"
        ));
        schema.put("additionalProperties", false);
        return schema;
    }
}
