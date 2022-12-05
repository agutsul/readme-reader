package com.agutsul.readme.reader;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;
import static org.apache.commons.lang3.StringUtils.lowerCase;
import static org.apache.commons.lang3.StringUtils.split;

public class DataServiceImpl implements DataService {

    @Override
    public Map<String, List<String>> analyze(Map<String, Optional<String>> data) {
        Map<String, List<String>> result = new LinkedHashMap<>();
        for (var entry : data.entrySet()) {
            if (entry.getValue().isEmpty()) {
                result.put(entry.getKey(), emptyList());
                continue;
            }

            String content = prepareContent(entry.getValue().get());
            result.put(entry.getKey(), countMostUsedWords(content));
        }
        return result;
    }

    private List<String> countMostUsedWords(String content) {
        Map<String, Long> stat = Stream.of(split(content," "))
                .collect(groupingBy(identity(), counting()));

        List<String> words = stat.entrySet().stream()
                .filter(statEntry -> statEntry.getKey().length() > 4)
                .sorted(comparing(Map.Entry::getValue, reverseOrder()))
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(toList());

        return words;
    }

    private String prepareContent(String string) {
        return lowerCase(string.replaceAll("[^a-zA-Z\\d' ]", ""));
    }
}
