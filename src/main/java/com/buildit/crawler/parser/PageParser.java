package com.buildit.crawler.parser;

import java.util.Set;

public interface PageParser {
  Set<String> parse(String uri);
}
