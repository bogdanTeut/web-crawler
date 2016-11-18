package com.buildit.crawler;

import com.buildit.crawler.parser.PageParser;

import java.util.Map;

public interface Crawler {
  void crawl(String uri);
  Map<String, String> getUrlsMap();

  void setPageParser(PageParser pageParser);
}
