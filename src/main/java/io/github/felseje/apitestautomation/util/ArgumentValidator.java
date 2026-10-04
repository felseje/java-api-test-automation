package io.github.felseje.apitestautomation.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ArgumentValidator {

  private static final String DEFAULT_NULL_MESSAGE = "Argument cannot be null";
  private static final String DEFAULT_BLANK_MESSAGE = "Argument cannot be blank";

  public static void requireNotNull(Object argument, String errorMessage) {
    if (argument != null) {
      return;
    }
    throw new IllegalArgumentException(
        Strings.isBlank(errorMessage) ? DEFAULT_NULL_MESSAGE : errorMessage
    );
  }

  public static void requireNotBlank(String argument, String errorMessage) {
    if (!Strings.isBlank(argument)) {
      return;
    }
    throw new IllegalArgumentException(
        Strings.isBlank(errorMessage) ? DEFAULT_BLANK_MESSAGE : errorMessage
    );
  }
}
