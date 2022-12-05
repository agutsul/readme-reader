package com.agutsul.readme.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.join;

public class ReadMeServiceImpl implements ReadMeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReadMeServiceImpl.class);

    private final GithubService githubService;
    private final ContentService contentService;
    private final DataService dataService;

    public ReadMeServiceImpl(String token) {
        this(new GithubServiceImpl(token), new ContentServiceImpl(), new DataServiceImpl());
    }

    ReadMeServiceImpl(GithubService githubService,
                      ContentService contentService,
                      DataService dataService) {
        this.githubService = githubService;
        this.contentService = contentService;
        this.dataService = dataService;
    }

    @Override
    public void printMostUsedWords() {
        try {
            List<String> urls = githubService.searchReadMeUrls();
            Map<String, Optional<String>> content = contentService.loadContent(urls);
            Map<String, List<String>> data = dataService.analyze(content);

            for (var entry : data.entrySet()) {
                System.out.println(String.format("%s\n[%s]",
                        entry.getKey(),
                        join(entry.getValue(), ",")
                ));
                System.out.println("===========================================");
            }
        } finally {
            contentService.close();
        }
    }
}
