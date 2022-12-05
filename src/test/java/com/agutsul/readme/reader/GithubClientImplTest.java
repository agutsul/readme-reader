package com.agutsul.readme.reader;

import com.spotify.github.v3.clients.GitHubClient;
import com.spotify.github.v3.clients.SearchClient;
import com.spotify.github.v3.repos.Repository;
import com.spotify.github.v3.search.SearchRepositories;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GithubClientImplTest {

    @Mock
    private GitHubClient gitHubClient;
    @InjectMocks
    private GithubClientImpl githubClient;

    @Test
    void testSearchReadMeUrlsReturningUrl() {
        var repo = mock(Repository.class);
        when(repo.htmlUrl()).thenReturn(URI.create("https://github.com/JoshBell302/CS344-OSI---Assignment-3-Small-Shell/"));
        when(repo.defaultBranch()).thenReturn("master");

        var searchRepo = mock(SearchRepositories.class);
        when(searchRepo.items()).thenReturn(List.of(repo));

        var searchClient = mock(SearchClient.class);
        when(searchClient.repositories(any()))
                .thenAnswer(inv -> CompletableFuture.completedFuture(searchRepo));

        when(gitHubClient.createSearchClient()).thenReturn(searchClient);

        List<String> urls = githubClient.searchReadMeUrls();
        assertFalse(urls.isEmpty());
        assertEquals("https://raw.githubusercontent.com/JoshBell302/CS344-OSI---Assignment-3-Small-Shell//master/README.md",
                urls.get(0));
    }

    @Test
    void testSearchReadMeUrlsReturnsEmptyItems() {
        var searchRepo = mock(SearchRepositories.class);
        when(searchRepo.items()).thenReturn(Collections.emptyList());

        var searchClient = mock(SearchClient.class);
        when(searchClient.repositories(any()))
                .thenAnswer(inv -> CompletableFuture.completedFuture(searchRepo));

        when(gitHubClient.createSearchClient()).thenReturn(searchClient);

        List<String> urls = githubClient.searchReadMeUrls();
        assertTrue(urls.isEmpty());
    }

    @Test
    void testSearchReadMeUrlsReturnsEmptyRepo() {
        var searchClient = mock(SearchClient.class);
        when(searchClient.repositories(any()))
                .thenAnswer(inv -> CompletableFuture.failedFuture(
                        new ExecutionException(new RuntimeException("test"))));

        when(gitHubClient.createSearchClient()).thenReturn(searchClient);

        List<String> urls = githubClient.searchReadMeUrls();
        assertTrue(urls.isEmpty());
    }
}
