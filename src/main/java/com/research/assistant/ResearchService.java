package com.research.assistant;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class ResearchService {

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public ResearchService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.build();
        this.objectMapper = objectMapper;
    }

    public String processContent(ResearchRequest request) {
        // Build the prompt
        String prompt = buildPrompt(request);

        // Gemini request format
        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", prompt)
                        ))
                )
        );

        // Call the Gemini API
        String fullUrl = geminiApiUrl + "?key=" + geminiApiKey;

        String response = webClient.post()
                .uri(fullUrl)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();


        System.out.println("Response from Gemini API: " + response);

        // Parse and extract text
        return extractTextFromResponse(response);
    }

    private String extractTextFromResponse(String response) {
        try {
            GeminiResponse geminiResponse = objectMapper.readValue(response, GeminiResponse.class);

            if (geminiResponse.getCandidates() != null && !geminiResponse.getCandidates().isEmpty()) {
                GeminiResponse.Candidate firstCandidate = geminiResponse.getCandidates().get(0);
                if (firstCandidate.getContent() != null &&
                        firstCandidate.getContent().getParts() != null &&
                        !firstCandidate.getContent().getParts().isEmpty()) {
                    return firstCandidate.getContent().getParts().get(0).getText();
                }
            }
            return "NO CONTENT FOUND IN RESPONSE";
        } catch (Exception e) {
            return "ERROR PARSING RESPONSE: " + e.getMessage();
        }
    }

    private String buildPrompt(ResearchRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request object is null");
        }
        if (request.getOperation() == null || request.getContent() == null) {
            throw new IllegalArgumentException("Operation or content is missing in the request");
        }

        StringBuilder prompt = new StringBuilder();

        switch (request.getOperation()) {
            case "summarize":
                prompt.append("You are a professional research assistant. Summarize the following content clearly and concisely. Focus only on the actual information present, avoid adding opinions, assumptions, or invented details. Organize the summary in short, coherent sentences or bullet points if appropriate.\n\n")
                        .append("Guidelines:\n")
                        .append("- Use no more than 5 sentences.\n")
                        .append("- Keep all facts verbatim; do not rephrase if it changes meaning.\n")
                        .append("- Highlight only the most important points.\n\n")
                        .append("Content:\n\"\"\"\n")
                        .append(request.getContent())
                        .append("\n\"\"\"\nFormat the response with clear headings and bullet points:\n\n");
                break;

            case "suggest":
                prompt.append("Based on the following content, suggest related topics and further reading. ")
                        .append("Format the response with clear headings and bullet points:\n\n")
                        .append(request.getContent());
                break;

            default:
                throw new IllegalArgumentException("Unknown operation: " + request.getOperation());
        }

        return prompt.toString();
    }

}
