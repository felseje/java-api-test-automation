package io.github.felseje.apitestautomation.service.login;

import io.github.felseje.apitestautomation.client.login.LoginClient;
import io.github.felseje.apitestautomation.client.login.dto.request.LoginRequest;
import io.github.felseje.apitestautomation.core.response.ApiResponse;
import io.github.felseje.apitestautomation.util.ArgumentValidator;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoginService {

  private final LoginClient loginClient;

  public ApiResponse login(LoginRequest request) {
    ArgumentValidator.requireNotNull(request, "The login request cannot be null");
    Response response = loginClient.login(request);
    return new ApiResponse(response);
  }

}
