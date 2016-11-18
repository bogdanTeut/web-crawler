package com.buildit.crawler.validation.impl;

import com.buildit.crawler.validation.UrlValidator;

/**
 * Default implementation for validator.
 * I agree the logic is minimal and could have been moved inside the crawler class
 * but from visibility reasons I chose to put it in a separate class.
 * I used the UrlValidator class from apache commons library. There might be better
 * solutions (we can even write out own regex) but because of simplicity reasons I chose to use this one.
 */
public class DefaultUrlValidator implements UrlValidator {

  private final org.apache.commons.validator.UrlValidator urlValidator;

  public DefaultUrlValidator() {
    this.urlValidator = new org.apache.commons.validator.UrlValidator();
  }

  @Override
  public boolean validate(final String uri) {
    return this.urlValidator.isValid(uri);
  }
}
