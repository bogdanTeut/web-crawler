package com.buildit.crawler;

import com.buildit.crawler.impl.WebCrawler;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the entry class
 * It can be run manually or java -jar can be used.
 */
public class Main {
  private final static Logger LOGGER = Logger.getLogger(Main.class.getSimpleName());

  public static void main(String[] args) {
    if (args.length < 1) throw  new IllegalArgumentException("Not enough parameters");
    final Crawler crawler = new WebCrawler();

    crawler.crawl(args[0]);

    crawler.getUrlsMap().entrySet().forEach(entry -> LOGGER.log(Level.INFO, entry.getKey()));
  }

}
