package com.shansown.androidarchitecture.data.mapper;

import javax.inject.Inject;
import javax.inject.Singleton;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

@Singleton
public final class DateTimeMapper {

  private static final DateTimeFormatter DATE_TIME_FORMAT =
      DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

  @Inject
  public DateTimeMapper() {
  }

  public DateTime toDateTime(String dateTimeString) {
    return DATE_TIME_FORMAT.parseDateTime(dateTimeString);
  }

  public String toString(DateTime dateTime) {
    return DATE_TIME_FORMAT.print(dateTime);
  }
}