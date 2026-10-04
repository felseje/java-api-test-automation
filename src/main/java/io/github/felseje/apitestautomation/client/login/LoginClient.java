package io.github.felseje.apitestautomation.client.login;

import io.github.felseje.apitestautomation.client.login.dto.request.LoginRequest;
import io.github.felseje.apitestautomation.core.client.AbstractClient;
import io.github.felseje.apitestautomation.core.request.RequestContext;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class LoginClient extends AbstractClient {

  public Response login(LoginRequest request) {
    RequestContext context = RequestContext.builder()
        .body(request)
        .contentType(ContentType.JSON)
        .build();
    return post(context, "/login");
  }

}
