package io.github.felseje.apitestautomation.client.user;

import io.github.felseje.apitestautomation.client.user.dto.request.CreateUserRequest;
import io.github.felseje.apitestautomation.client.user.dto.request.UpdateUserRequest;
import io.github.felseje.apitestautomation.core.client.AbstractClient;
import io.github.felseje.apitestautomation.core.request.RequestContext;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.Map;

public class UserClient extends AbstractClient {

  private static final String USERS_ENDPOINT = "/usuarios";
  private static final String USERS_ID_ENDPOINT = USERS_ENDPOINT + "/{userId}";

  public Response createUser(CreateUserRequest request) {
    RequestContext requestContext = RequestContext.builder()
        .contentType(ContentType.JSON)
        .body(request)
        .build();
    return post(requestContext, USERS_ENDPOINT);
  }

  public Response getUser(String id) {
    RequestContext requestContext = getUserIdContext(id);
    return get(requestContext, USERS_ID_ENDPOINT);
  }

  public Response getUsers(Map<String, Object> queryParams) {
    var requestContextBuilder = RequestContext.builder();
    queryParams.forEach(requestContextBuilder::queryParam);
    return get(requestContextBuilder.build(), USERS_ENDPOINT);
  }

  public Response updateUser(String id, UpdateUserRequest request) {
    RequestContext requestContext = RequestContext.builder()
        .contentType(ContentType.JSON)
        .body(request)
        .pathParam("userId", id)
        .build();
    return put(requestContext, USERS_ID_ENDPOINT);
  }

  public Response deleteUser(String id) {
    RequestContext requestContext = getUserIdContext(id);
    return delete(requestContext, USERS_ID_ENDPOINT);
  }

  private RequestContext getUserIdContext(final String userId) {
    return RequestContext.builder().pathParam("userId", userId).build();
  }
}
