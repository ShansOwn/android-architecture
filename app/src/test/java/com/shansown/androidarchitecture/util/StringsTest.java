package com.shansown.androidarchitecture.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;

public class StringsTest {

  @Rule public final ExpectedException exception = ExpectedException.none();

  @Test public void testIsBlank() throws Exception {
    assertTrue("Null is also blank", Strings.isBlank(null));

    assertTrue("Empty string is blank", Strings.isBlank(""));

    assertTrue("String only with spaces is blank", Strings.isBlank("   "));
  }

  @Test public void testValueOrDefault() throws Exception {
    assertEquals("If value is null should return default value",
        Strings.valueOrDefault(null, "def"), "def");

    assertEquals("If value is empty string should return default value",
        Strings.valueOrDefault("", "def"), "def");

    assertEquals("If value is string only with spaces should return default value",
        Strings.valueOrDefault("", "def"), "def");

    assertEquals("If value is correct string should return value",
        Strings.valueOrDefault("correct", "def"), "correct");
  }

  @Test public void testTruncateAt() throws Exception {
    try {
      Strings.truncateAt(null, anyInt());
      fail("TruncateAt for null should throw exception");
    } catch (Exception e) {
      assertEquals("TruncateAt for null should throw NPE", e.getClass(),
          NullPointerException.class);
    }

    assertEquals("TruncateAt for empty string should return empty string",
        Strings.truncateAt("", anyInt()), "");

    assertEquals("TruncateAt for string length of which <= length param should return same string",
        Strings.truncateAt("abc", 3), "abc");

    assertEquals("TruncateAt expected result with substring",
        Strings.truncateAt("abc", 2), "ab");
  }
}