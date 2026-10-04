package io.github.felseje.apitestautomation.util;

import java.util.Locale;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.datafaker.Faker;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DataFaker {

  private static final Faker FAKER = new Faker(Locale.of("pt", "BR"));

  public static String getFirstName() {
    return FAKER.name().firstName();
  }

  public static String getLastName() {
    return FAKER.name().lastName();
  }

  public static String getFullName() {
    return FAKER.name().fullName();
  }

  public static String getUsername() {
    return FAKER.credentials().username();
  }

  public static String getEmail() {
    return FAKER.internet().emailAddress();
  }

  public static String getPassword() {
    return FAKER.credentials().password();
  }

  public static String getRandomAlphanumericString(int length) {
    return FAKER.regexify("[a-zA-Z0-9]{" + length + "}");
  }
}
