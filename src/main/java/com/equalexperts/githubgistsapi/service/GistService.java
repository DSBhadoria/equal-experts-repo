package com.equalexperts.githubgistsapi.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class GistService {

    private final WebClient webClient;

    public GistService() {
        this.webClient = WebClient.create("https://api.github.com");
    }

    // Constructor for dependency injection (for testing or custom config)
    public GistService(WebClient webClient) {
        this.webClient = webClient != null ? webClient : WebClient.create("https://api.github.com");
    }

    public String getUserGists(Integer page, Integer per_page) {

        page = (page != null && page > 0) ?  page : 0;
        per_page = (per_page != null && per_page > 0) ? per_page : 30;

        try {
            return webClient.get()
                    .uri("/gists/public?page=" + page + "&per_page=" + per_page)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

        } catch (WebClientResponseException e) {
            throw new RuntimeException("Failed to fetch gists: " + e.getMessage());
        }
    }
}
