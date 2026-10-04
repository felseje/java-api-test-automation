package io.github.felseje.apitestautomation.util;

import java.text.Normalizer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Strings {

  public static boolean isBlank(String value) {
    return value == null || value.isBlank();
  }

  public static String normalize(String string) {
    return Normalizer.normalize(string, Normalizer.Form.NFD)
        .replaceAll("\\p{M}", "")
        .replaceAll("[^a-zA-Z0-9]", "");
  }

}
