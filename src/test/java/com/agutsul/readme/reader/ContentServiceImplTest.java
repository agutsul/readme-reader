package com.agutsul.readme.reader;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContentServiceImplTest {

    @Mock
    private CloseableHttpClient httpClient;
    @InjectMocks
    private ContentServiceImpl contentService;

    @Test
    void testLoadContent() throws IOException {
        when(httpClient.execute(
                any(HttpGet.class),
                any(BasicHttpClientResponseHandler.class)
        )).thenReturn("dummy data");

        var data = contentService.loadContent(List.of("anyUrl"));
        assertEquals("dummy data", data.get("anyUrl").get());
    }

    @Test
    @Disabled
    void testLoadContentWithError() throws IOException {
        doThrow(new RuntimeException("test"))
                .when(httpClient).execute(
                        any(HttpGet.class),
                        any(BasicHttpClientResponseHandler.class)
                );

        var data = contentService.loadContent(List.of("anyUrl"));
        assertTrue(data.get("anyUrl").isEmpty());
    }
}
