package io.github.felseje.apitestautomation.testdata;

import io.github.felseje.apitestautomation.client.login.dto.request.LoginRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LoginProvider {

  private static final String VALID_EMAIL = "fulano@qa.com";
  private static final String VALID_PASSWORD = "teste";

  public static LoginRequest getValidLoginRequest() {
    return new LoginRequest(VALID_EMAIL, VALID_PASSWORD);
  }

  public static LoginRequest getLoginRequestWithUnauthorizedCredentials() {
    return new LoginRequest("unauth@mail.com", "p4ssw0rd");
  }

  public static LoginRequest getLoginRequestWithEmptyEmail() {
    return new LoginRequest("", "p4ssw0rd");
  }

  public static LoginRequest getLoginRequestWithEmptyPassword() {
    return new LoginRequest("rTyxUuv@qa.com", "");
  }

}
