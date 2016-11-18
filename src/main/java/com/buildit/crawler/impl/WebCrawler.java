package com.buildit.crawler.impl;

import com.buildit.crawler.Crawler;
import com.buildit.crawler.normalization.UrlNormalizer;
import com.buildit.crawler.normalization.impl.DefaultUrlNormalizer;
import com.buildit.crawler.parser.PageParser;
import com.buildit.crawler.parser.impl.DefaultPagePageParser;
import com.buildit.crawler.validation.UrlValidator;
import com.buildit.crawler.validation.impl.DefaultUrlValidator;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebCrawler implements Crawler {
  private final static Pattern PATTERN =  Pattern.compile("(https?://)([^:^/]*)(:\\d*)?(.*)?");
  private final Map<String, String> urlsMap;
  private final UrlValidator urlValidator;
  private PageParser pageParser;
  private final List<String> urlsToVisit;
  private final UrlNormalizer urlNormalizer;

  public WebCrawler() {
    this.urlsMap = new HashMap<>();
    this.urlValidator = new DefaultUrlValidator();
    this.pageParser = new DefaultPagePageParser();
    this.urlsToVisit = new LinkedList<>();
    this.urlNormalizer = new DefaultUrlNormalizer();
  }

  /**
   * This contains the logic skeleton.
   * Breadth First Search algorithm by using a LinkedList as a underlying collection.
   * @param uri
   */
  @Override
  public void crawl(final String uri) {
    validateUri(uri);

    //Breadth First Search
    final String domain = extractDomain(uri);

    this.urlsToVisit.add(uri);

    do {
      final String urlToParse = this.urlsToVisit.remove(0);
      final String normalizedUrl = this.urlNormalizer.normalize(urlToParse);
      this.urlsMap.put(normalizedUrl, normalizedUrl);

      if (isParsable(normalizedUrl, domain)) {
        final Set<String> urls = this.pageParser.parse(normalizedUrl);
        this.urlsToVisit.addAll(urls);
      }
    } while (!this.urlsToVisit.isEmpty());

  }

  /**
   * Extracts the domain.
   * Relatively cheap operation, since the expensive operation (Pattern.compile) is done only once.
   * @param uri
   * @return
   */
  private String extractDomain(final String uri) {
    final Matcher matcher = PATTERN.matcher(uri);
    matcher.find();
    //using split is not the best solution, I couldn't make the regex work in time
    final String domain = matcher.group(2).split("\\.", 2)[1];
    return domain;
  }

  /**
   * Extracts the extension
   * Relatively cheap operation, since the expensive operation (Pattern.compile) is done only once.
   * @param uri
   * @return
   */
  private String extractExtension(final String uri) {
    final Matcher matcher = PATTERN.matcher(uri);
    matcher.find();
    final String extension = matcher.group(4);
    return extension;
  }

  /**
   * Determines if the url is parsable or not.
   * The domains have to match also the file extension should be an accepted one.
   * E.g. www.something.com/logo.gif is not parsable.
   * @param uri
   * @param domain
   * @return
   */
  private boolean isParsable(final String uri, final String domain) {
    final String domainToBeChecked = extractDomain(uri);
    final String extension = extractExtension(uri);

    final boolean acceptedExtension = StringUtils.isNotBlank(extension)?extension.contains("html"):true;

    return domainToBeChecked.contains(domain) && acceptedExtension;
  }

  /**
   * Validates URI
   * @param uri
   */
  private void validateUri(final String uri) {

    final boolean validate = this.urlValidator.validate(uri);
    if (!validate) throw new IllegalArgumentException("Bad uri was provided");
  }

  @Override
  public Map<String, String> getUrlsMap() {
    return urlsMap;
  }

  @Override
  public void setPageParser(final PageParser pageParser) {
    this.pageParser = pageParser;
  }
}
