package com.buildit.crawler.normalization.impl;

import com.buildit.crawler.normalization.UrlNormalizer;

/**
 * Default implementation for UrlNormalizer.
 * I agree the logic is minimal and could have been moved inside the crawler class
 * but from visibility reasons I chose to put it in a separate class.
 * A lot of things need to be taken into consideration here:
 * 1) Capitalizing letters in escape sequences.
 * 2) Decoding percent-encoded octets of unreserved characters
 * 3) Removing the default port
 * 4) Treating the anchors.
 * etc...
 *
 * Because of time constraints reasons I added only the basic lowercase.
 */
public class DefaultUrlNormalizer implements UrlNormalizer {

  @Override
  public String normalize(String uri) {
    return uri.toLowerCase();
  }
}
