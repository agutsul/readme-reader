package com.agutsul.readme.reader;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class DataServiceImplTest {

    @InjectMocks
    private DataServiceImpl dataService;

    @Test
    void testAnalyze() {
        Map<String, Optional<String>> data = new HashMap<>();
        data.put("url1", Optional.empty());
        data.put("url2", Optional.of(
                "test1 test2 test3 test1 test2 test3 " +
                        "test1 test2 test3 test1 test2 test3 " +
                        "test test4 abc"));

        Map<String, List<String>> favoriteWordsPerUrl = dataService.analyze(data);
        assertEquals(2, favoriteWordsPerUrl.size());

        assertTrue(favoriteWordsPerUrl.get("url1").isEmpty());
        assertTrue(favoriteWordsPerUrl.get("url2").containsAll(List.of("test1", "test2", "test3")));
    }
}
