package com.agutsul.readme.reader;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DataService {
    Map<String, List<String>> analyze(Map<String, Optional<String>> data);
}
