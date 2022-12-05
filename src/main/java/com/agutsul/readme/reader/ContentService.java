package com.agutsul.readme.reader;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ContentService {
    Map<String, Optional<String>> loadContent(List<String> urls);
    void close();
}
