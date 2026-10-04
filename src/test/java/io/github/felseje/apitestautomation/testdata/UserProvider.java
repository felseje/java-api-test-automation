package io.github.felseje.apitestautomation.testdata;

import io.github.felseje.apitestautomation.client.user.dto.UserDto;
import io.github.felseje.apitestautomation.client.user.dto.request.CreateUserRequest;
import io.github.felseje.apitestautomation.client.user.dto.request.UpdateUserRequest;
import io.github.felseje.apitestautomation.util.DataFaker;
import io.github.felseje.apitestautomation.util.Strings;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserProvider {

  public static CreateUserRequest getValidCreateUserRequest() {
    return createUser(CreateUserRequest::new);
  }

  public static UpdateUserRequest getValidUpdateUserRequest() {
    return createUser(UpdateUserRequest::new);
  }

  public static String getValidRandomUserId() {
    return "T3sT" + DataFaker.getRandomAlphanumericString(12);
  }

  private static <T extends UserDto> T createUser(
      QuadFunction<String, String, String, String, T> constructor
  ) {
    final String firstName = DataFaker.getFirstName();
    final String lastName = DataFaker.getLastName();
    final String username =
        Strings.normalize(firstName + lastName).toLowerCase();
    final String email =
        username + "@" + DataFaker.getEmail().split("@")[1];
    final String password = DataFaker.getPassword();

    return constructor.apply(
        firstName + " " + lastName,
        email,
        password,
        "false"
    );
  }

  @FunctionalInterface
  private interface QuadFunction<A, B, C, D, R> {

    R apply(A a, B b, C c, D d);
  }
}
