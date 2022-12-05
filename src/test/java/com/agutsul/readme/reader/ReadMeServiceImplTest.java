package com.agutsul.readme.reader;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import static java.util.Collections.*;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ReadMeServiceImplTest {

    @Mock
    private GithubClient githubClient;
    @Mock
    private ContentService contentService;
    @Mock
    private DataService dataService;

    @InjectMocks
    private ReadMeServiceImpl readMeService;

    @Test
    void tesPrintMostUsedWords() throws IOException {
        when(githubClient.searchReadMeUrls()).thenReturn(List.of("url"));
        when(contentService.loadContent(any()))
                .thenReturn(singletonMap("url", Optional.of("words")));
        when(dataService.analyze(any()))
                .thenReturn(singletonMap("url", List.of("words")));

        try (ByteArrayOutputStream bo = new ByteArrayOutputStream();
             PrintStream printStream = new PrintStream(bo)) {
            System.setOut(printStream);

            readMeService.printMostUsedWords();
            bo.flush();

            String logs = new String(bo.toByteArray());
            assertTrue(logs.contains("url"));
            assertTrue(logs.contains("[words]"));
        }
    }
}
