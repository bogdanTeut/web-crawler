package com.buildit.crawler.parser.impl;

import com.buildit.crawler.parser.PageParser;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Default implementation for PageParser.
 * Because of time constraints reasons I added only the basic functionality.
 */
public class DefaultPagePageParser implements PageParser {

  @Override
  public Set<String> parse(String uri) {
    final Set<String> links = new HashSet<>();

    try {
      final Connection connection = Jsoup.connect(uri);
      final Document htmlDocument = connection.get();

      final Elements linksOnPage = htmlDocument.select("a[href]");
      for (Element link : linksOnPage) {
        links.add(link.absUrl("href"));
      }
    } catch(final IOException ioe) {
      //something to do here
    }

    return links;
  }
}
