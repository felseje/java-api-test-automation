package io.github.felseje.apitestautomation.core.request;

import io.github.felseje.apitestautomation.core.response.ApiResponse;
import io.github.felseje.apitestautomation.util.ArgumentValidator;
import java.util.function.Supplier;

public class ApiRequest {

  private final Supplier<ApiResponse> action;

  public ApiRequest(Supplier<ApiResponse> action) {
    ArgumentValidator.requireNotNull(action, "The 'action' cannot be null");
    this.action = action;
  }

  public ApiResponse execute() {
    return action.get();
  }

  public RetryRequest retry() {
    return new RetryRequest(action);
  }

}
