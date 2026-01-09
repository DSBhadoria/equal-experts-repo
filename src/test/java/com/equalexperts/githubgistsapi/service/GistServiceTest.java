package com.equalexperts.githubgistsapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class GistServiceTest {
    @Mock
    private WebClient webClient;
    @Mock
    private RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock
    private RequestHeadersSpec requestHeadersSpec;
    @Mock
    private ResponseSpec responseSpec;
    @InjectMocks
    private GistService gistService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        gistService = new GistService(webClient);
    }

    @Test
    void getUserGists_returnsGists() {
        String expected = "[{'id':'1'}]";
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(expected));

        String result = gistService.getUserGists(1, 10);
        assertEquals(expected, result);
    }

    @Test
    void getUserGists_nullParams_usesDefaults() {
        String expected = "[]";
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(contains("page=0"))).thenReturn(requestHeadersSpec);
        when(requestHeadersUriSpec.uri(contains("per_page=30"))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(expected));

        String result = gistService.getUserGists(null, null);
        assertEquals(expected, result);
    }

    @Test
    void getUserGists_webClientThrows_exceptionThrown() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        // Mock the exception and provide a message
        WebClientResponseException exception = mock(WebClientResponseException.class);
        when(exception.getMessage()).thenReturn("Some error");
        when(responseSpec.bodyToMono(String.class)).thenThrow(exception);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> gistService.getUserGists(1, 1));
        assertTrue(ex.getMessage() != null && ex.getMessage().startsWith("Failed to fetch gists"));
    }

}

