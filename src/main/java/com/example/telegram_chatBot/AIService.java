package com.example.telegram_chatBot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.Exception;

@Service
public class AIService {

    @Value("${openai.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper = new ObjectMapper();

    public Object getResponse(String userText) {

        if (apiKey == null || apiKey.isBlank()) {
            return "Xatolik: API KEY topilmadi ❌";
        }

        try {
            String url = "https://api.openai.com/v1/responses";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey.trim());

            String requestBody = objectMapper.writeValueAsString(
                    new RequestBody("gpt-4.1-mini", userText)
            );

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            return extractText(response.getBody());

        } catch (Exception e) {
            e.printStackTrace();
            return "Xatolik: " + e.getMessage();
        }
    }

    private String extractText(String json) {
        try {
            JsonNode root = objectMapper.readTree(json);

            JsonNode output = root.path("output");

            if (output.isArray() && output.size() > 0) {
                JsonNode content = output.get(0).path("content");

                if (content.isArray() && content.size() > 0) {
                    return content.get(0).path("text").asText("Javob topilmadi");
                }
            }

            return "Javob topilmadi";

        } catch (Exception e) {
            return "Parsing xatolik ❌";
        }
    }

    static class RequestBody {
        public String model;
        public String input;

        public RequestBody(String model, String input) {
            this.model = model;
            this.input = input;
        }
    }

}