package io.github.felseje.apitestautomation.test;

import io.github.felseje.apitestautomation.client.login.LoginClient;
import io.github.felseje.apitestautomation.client.login.dto.request.LoginRequest;
import io.github.felseje.apitestautomation.core.response.ApiResponse;
import io.github.felseje.apitestautomation.service.login.LoginService;
import io.github.felseje.apitestautomation.testdata.LoginProvider;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@Tag("login")
@DisplayName("Login Tests")
public class LoginIT {

  private final LoginService loginService = new LoginService(new LoginClient());

  @Nested
  @DisplayName("Successful login scenarios")
  class SuccessfulLogin {

    private static final String LOGIN_SUCCESS_SCHEMA = "schemas/login/success.json";

    @Test
    @DisplayName("Should authenticate successfully when valid credentials are provided")
    void shouldAuthenticateSuccessfully() {
      // Arrange
      LoginRequest request = LoginProvider.getValidLoginRequest();

      // Act
      ApiResponse response = loginService.login(request);

      // Assert
      response.shouldBeOk()
          .shouldMatchJsonSchemaInClasspath(LOGIN_SUCCESS_SCHEMA);
    }

  }

  @Nested
  @DisplayName("Failed login scenarios")
  class FailedLogin {

    private static final String LOGIN_UNAUTHORIZED_SCHEMA = "schemas/login/unauthorized.json";
    private static final String LOGIN_EMPTY_EMAIL_SCHEMA = "schemas/login/empty-email.json";
    private static final String LOGIN_EMPTY_PASSWORD_SCHEMA = "schemas/login/empty-password.json";

    static Stream<Arguments> provideEmptyLoginFieldsForBadRequest() {
      return Stream.of(
          Arguments.of(
              "Should return Bad Request when email is empty",
              LOGIN_EMPTY_EMAIL_SCHEMA,
              LoginProvider.getLoginRequestWithEmptyEmail()
          ),
          Arguments.of(
              "Should return Bad Request when password is empty",
              LOGIN_EMPTY_PASSWORD_SCHEMA,
              LoginProvider.getLoginRequestWithEmptyPassword()
          )
      );
    }

    @Test
    @DisplayName("Should return unauthorized when invalid credentials are provided")
    void shouldReturnUnauthorizedWhenCredentialsAreInvalid() {
      // Arrange
      LoginRequest request = LoginProvider.getLoginRequestWithUnauthorizedCredentials();

      // Act
      ApiResponse response = loginService.login(request);

      // Assert
      response.shouldBeUnauthorized()
          .shouldMatchJsonSchemaInClasspath(LOGIN_UNAUTHORIZED_SCHEMA);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideEmptyLoginFieldsForBadRequest")
    void shouldReturnBadRequestForEmptyFields(String testName, String expectedSchema,
        LoginRequest request) {
      // Act
      ApiResponse response = loginService.login(request);

      // Assert
      response.shouldBeBadRequest()
          .shouldMatchJsonSchemaInClasspath(expectedSchema);
    }

  }

}
