package com.agutsul.readme.reader;

import com.spotify.github.v3.clients.GitHubClient;
import com.spotify.github.v3.search.SearchRepositories;
import com.spotify.github.v3.search.requests.ImmutableSearchParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.replace;

public class GithubServiceImpl implements GithubService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GithubServiceImpl.class);
    private static final String API_GITHUB_COM = "https://api.github.com/";

    private static final String README_URL_FORMAT = "%s/%s/README.md";

    private final GitHubClient githubClient;

    public GithubServiceImpl(String token) {
        this(GitHubClient.create(URI.create(API_GITHUB_COM), token));
    }

    GithubServiceImpl(GitHubClient githubClient) {
        this.githubClient = githubClient;
    }

    @Override
    public List<String> searchReadMeUrls() {
        var params = ImmutableSearchParameters.builder()
                .q("filename:README") //  extension:md
                .build();

        SearchRepositories repositories = null;
        try {
            var searchClient = githubClient.createSearchClient();
            var searchFuture = searchClient.repositories(params);

            repositories = searchFuture.get();
        } catch (InterruptedException e) {
            LOGGER.error("Interrupted exception", e);
        } catch (ExecutionException e) {
            LOGGER.error("Unexpected exception", e);
        }

        if (repositories == null || repositories.items() == null) {
            return emptyList();
        }

        return repositories.items().stream()
                .map(repo -> String.format(README_URL_FORMAT, repo.htmlUrl(), repo.defaultBranch()))
                .map(url -> replace(url, "github.com", "raw.githubusercontent.com"))
                .collect(toList());
    }
}
