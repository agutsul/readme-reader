package com.agutsul.readme.reader;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

public class ContentServiceImpl implements ContentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentServiceImpl.class);

    private final CloseableHttpClient httpClient;

    public ContentServiceImpl() {
        this(HttpClients.createDefault());
    }

    ContentServiceImpl(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public Map<String, Optional<String>> loadContent(List<String> urls) {
        Map<String, Optional<String>> data = new LinkedHashMap<>();
        for (String url : urls) {
            data.put(url, readData(url));
        }
        return data;
    }

    // should be marked as @PreDestroy
    public void close() {
        try {
            httpClient.close();
        } catch (IOException e) {
            LOGGER.error("Unable to close HttpClient", e);
        }
    }

    private Optional<String> readData(String url) {
        try {
            String data = httpClient.execute(
                    new HttpGet(url),
                    new BasicHttpClientResponseHandler()
            );

            return Optional.of(data);
        } catch (IOException e) {
            LOGGER.error("Unable to read data for '{}': {}", url, ExceptionUtils.getMessage(e));
        }

        return Optional.empty();
    }
}
