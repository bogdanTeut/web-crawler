package com.buildit.crawler;

import com.buildit.crawler.impl.WebCrawler;
import com.buildit.crawler.parser.PageParser;
import com.google.common.collect.ImmutableSet;
import org.testng.annotations.Test;

import java.util.Map;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class WebCrawlerTest {

  @Test
  public void testWebCrawlerCreationAndBasics() {
    //creation
    final Crawler webCrawler = new WebCrawler();

    //prepare mocks
    final PageParser pageParser = mock(PageParser.class);
    when(pageParser.parse(anyString())).thenReturn(ImmutableSet.of());

    webCrawler.setPageParser(pageParser);

    //test crawling functionality
    webCrawler.crawl("http://somewebsite.com");

    //verify results
    final Map<String, String> linksMap = webCrawler.getUrlsMap();
    assertNotNull(linksMap);
  }

  @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Bad uri was provided")
  public void givenInvalidUri_whenCrawling_thenThrowIllegalArgumentException() {
    //preparing object
    final Crawler webCrawler = new WebCrawler();

    //method under test
    webCrawler.crawl("htt://somewebsite.com");
  }

  @Test
  public void whenCrawling_thenReturnTheCompleteUrlsMap() {
    //preparing object
    final Crawler webCrawler = new WebCrawler();

    //prepare mocks
    final PageParser pageParser = mock(PageParser.class);
    when(pageParser.parse(anyString())).thenReturn(ImmutableSet.of("http://someotherswebsite.buildit.com", "http://alsothiswebsite.buildit.com"))
                                    .thenReturn(ImmutableSet.of());

    webCrawler.setPageParser(pageParser);

    //method under test
    webCrawler.crawl("http://somewebsite.buildit.com");

    //verify results
    final Map<String, String> linksMap = webCrawler.getUrlsMap();
    assertNotNull(linksMap);
    assertTrue(linksMap.containsKey("http://somewebsite.buildit.com"));
    assertTrue(linksMap.containsKey("http://someotherswebsite.buildit.com"));
    assertTrue(linksMap.containsKey("http://alsothiswebsite.buildit.com"));
  }

  /**
   * This test doesn't makes sense at first view since we are using map to store the elements, but thinking in perspective
   * let's imagine someone changes the underneath collection from map to a list, in that case you definitely want this test around
   */
  @Test
  public void givenDuplicateUrls_whenCrawling_thenUrlsMapContainsOnlyOneUrl() {
    //preparing object
    final Crawler webCrawler = new WebCrawler();

    //prepare mocks
    final PageParser pageParser = mock(PageParser.class);
    when(pageParser.parse(anyString())).thenReturn(ImmutableSet.of("http://childwebsiteone.buildit.com", "http://childwebsitetwo.buildit.com"))
            .thenReturn(ImmutableSet.of("http://someotherswebsite.buildit.com", "http://duplicatewebsite.buildit.com"))
            .thenReturn(ImmutableSet.of("http://duplicatewebsite.buildit.com", "http://alsothiswebsite.buildit.com"))
            .thenReturn(ImmutableSet.of());

    webCrawler.setPageParser(pageParser);

    //method under tests
    webCrawler.crawl("http://somewebsite.buildit.com");

    //verify results
    final Map<String, String> linksMap = webCrawler.getUrlsMap();
    assertNotNull(linksMap);
    assertTrue(linksMap.containsKey("http://somewebsite.buildit.com"));
    assertTrue(linksMap.containsKey("http://childwebsiteone.buildit.com"));
    assertTrue(linksMap.containsKey("http://childwebsitetwo.buildit.com"));
    assertTrue(linksMap.containsKey("http://someotherswebsite.buildit.com"));
    assertTrue(linksMap.containsKey("http://alsothiswebsite.buildit.com"));
    assertTrue(linksMap.containsKey("http://duplicatewebsite.buildit.com"));
  }

  @Test
  public void givenDuplicateNormalizedUrls_whenCrawling_thenUrlsMapContainsOnlyOneNormalizedUrl() {
    //preparing object
    final Crawler webCrawler = new WebCrawler();

    //prepare mocks
    final PageParser pageParser = mock(PageParser.class);
    when(pageParser.parse(anyString())).thenReturn(ImmutableSet.of("http://childwebsiteone.buildit.com", "http://childwebsitetwo.buildit.com"))
            .thenReturn(ImmutableSet.of("http://someotherswebsite.buildit.com", "http://duplicatewebsite.buildit.com/a%c2%b1b"))
            .thenReturn(ImmutableSet.of("http://DUPLICATEWEBSITE.buildit.com/A%C2%B1B", "http://alsothiswebsite.buildit.com"))
            .thenReturn(ImmutableSet.of());

    webCrawler.setPageParser(pageParser);

    //method under test
    webCrawler.crawl("http://somewebsite.buildit.com");

    //verify results
    final Map<String, String> linksMap = webCrawler.getUrlsMap();
    assertNotNull(linksMap);
    assertTrue(linksMap.containsKey("http://somewebsite.buildit.com"));
    assertTrue(linksMap.containsKey("http://childwebsiteone.buildit.com"));
    assertTrue(linksMap.containsKey("http://childwebsitetwo.buildit.com"));
    assertTrue(linksMap.containsKey("http://someotherswebsite.buildit.com"));
    assertTrue(linksMap.containsKey("http://alsothiswebsite.buildit.com"));
    assertTrue(linksMap.containsKey("http://duplicatewebsite.buildit.com/a%c2%b1b"));
    assertFalse(linksMap.containsKey("http://DUPLICATEWEBSITE.buildit.com/A%C2%B1B"));
  }

  @Test
  public void givenExternalUrls_whenCrawling_thenUrlsMapContainThemButNotTheirChildren() {
    //preparing object
    final Crawler webCrawler = new WebCrawler();

    //prepare mocks
    final PageParser pageParser = mock(PageParser.class);
    when(pageParser.parse("http://somewebsite.buildit.com")).thenReturn(ImmutableSet.of("http://childwebsiteone.buildit.com", "http://childwebsitetwo.buildit.com"));
    when(pageParser.parse("http://childwebsiteone.buildit.com")).thenReturn(ImmutableSet.of("http://someotherswebsite.buildit.com", "http://duplicatewebsite.buildit.com"));
    when(pageParser.parse("http://childwebsitetwo.buildit.com")).thenReturn(ImmutableSet.of("http://externalurl.externaldomain.com", "http://alsothiswebsite.buildit.com"));
    when(pageParser.parse("http://externalurl.externaldomain.com")).thenReturn(ImmutableSet.of("http://externalurlsiteone.externaldomain.com", "http://externalurlsitetwo.buildit.com"));

    when(pageParser.parse("http://someotherswebsite.buildit.com")).thenReturn(ImmutableSet.of());
    when(pageParser.parse("http://duplicatewebsite.buildit.com/a%c2%b1b")).thenReturn(ImmutableSet.of());
    when(pageParser.parse("http://alsothiswebsite.buildit.com")).thenReturn(ImmutableSet.of());

    webCrawler.setPageParser(pageParser);

    //method under test
    webCrawler.crawl("http://somewebsite.buildit.com");

    //verify results
    final Map<String, String> linksMap = webCrawler.getUrlsMap();
    assertNotNull(linksMap);
    assertTrue(linksMap.containsKey("http://somewebsite.buildit.com"));
    assertTrue(linksMap.containsKey("http://childwebsiteone.buildit.com"));
    assertTrue(linksMap.containsKey("http://childwebsitetwo.buildit.com"));
    assertTrue(linksMap.containsKey("http://someotherswebsite.buildit.com"));
    assertTrue(linksMap.containsKey("http://alsothiswebsite.buildit.com"));
    assertTrue(linksMap.containsKey("http://duplicatewebsite.buildit.com"));
    assertTrue(linksMap.containsKey("http://externalurl.externaldomain.com"));
    assertFalse(linksMap.containsKey("http://externalurlsiteone.externaldomain.com"));
    assertFalse(linksMap.containsKey("http://externalurlsitetwo.externaldomain.com"));
  }

  @Test
  public void givenNonHtmlUrl_whenCrawling_thenUrlsMapContainItButItIsNotParsed() {
    //preparing object
    final Crawler webCrawler = new WebCrawler();

    //prepare mocks
    final PageParser pageParser = mock(PageParser.class);
    when(pageParser.parse("http://somewebsite.buildit.com")).thenReturn(ImmutableSet.of("http://childwebsiteone.buildit.com", "http://childwebsitetwo.buildit.com"));
    when(pageParser.parse("http://childwebsiteone.buildit.com")).thenReturn(ImmutableSet.of("http://someotherswebsite.buildit.com", "http://duplicatewebsite.buildit.com"));
    when(pageParser.parse("http://childwebsitetwo.buildit.com")).thenReturn(ImmutableSet.of("http://childwebsiteone.buildit.com/logo.gif", "http://alsothiswebsite.buildit.com"));

    when(pageParser.parse("http://someotherswebsite.buildit.com")).thenReturn(ImmutableSet.of());
    when(pageParser.parse("http://duplicatewebsite.buildit.com/a%c2%b1b")).thenReturn(ImmutableSet.of());
    when(pageParser.parse("http://alsothiswebsite.buildit.com")).thenReturn(ImmutableSet.of());

    webCrawler.setPageParser(pageParser);

    //method under test
    webCrawler.crawl("http://somewebsite.buildit.com");

    //verify results
    final Map<String, String> linksMap = webCrawler.getUrlsMap();
    assertNotNull(linksMap);
    assertTrue(linksMap.containsKey("http://somewebsite.buildit.com"));
    assertTrue(linksMap.containsKey("http://childwebsiteone.buildit.com"));
    assertTrue(linksMap.containsKey("http://childwebsitetwo.buildit.com"));
    assertTrue(linksMap.containsKey("http://someotherswebsite.buildit.com"));
    assertTrue(linksMap.containsKey("http://alsothiswebsite.buildit.com"));
    assertTrue(linksMap.containsKey("http://duplicatewebsite.buildit.com"));
    assertTrue(linksMap.containsKey("http://childwebsiteone.buildit.com/logo.gif"));

    //verify the non html url has been parsed
    verify(pageParser, never()).parse("http://childwebsiteone.buildit.com/logo.gif");
  }
}
