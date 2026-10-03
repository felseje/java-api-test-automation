package io.github.felseje.apitestautomation.service.user;

import io.github.felseje.apitestautomation.client.user.UserClient;
import io.github.felseje.apitestautomation.client.user.dto.request.CreateUserRequest;
import io.github.felseje.apitestautomation.client.user.dto.request.UpdateUserRequest;
import io.github.felseje.apitestautomation.core.response.ApiResponse;
import io.github.felseje.apitestautomation.util.ArgumentValidator;
import io.restassured.response.Response;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserService {

  private final UserClient userClient;

  public ApiResponse createUser(CreateUserRequest requestBody) {
    ArgumentValidator.requireNotNull(requestBody, "The request body cannot be null");
    Response response = userClient.createUser(requestBody);
    return new ApiResponse(response);
  }

  public ApiResponse readUser(String userId) {
    Response response = userClient.getUser(userId);
    return new ApiResponse(response);
  }

  public ApiResponse readAllUsers(Map<String, Object> queryParams) {
    ArgumentValidator.requireNotNull(queryParams, "The query params cannot be null");
    Response response = userClient.getUsers(queryParams);
    return new ApiResponse(response);
  }

  public ApiResponse updateUser(String userId, UpdateUserRequest requestBody) {
    ArgumentValidator.requireNotNull(requestBody, "The request body cannot be null");
    Response response = userClient.updateUser(userId, requestBody);
    return new ApiResponse(response);
  }

  public ApiResponse deleteUser(String userId) {
    Response response = userClient.deleteUser(userId);
    return new ApiResponse(response);
  }
}
