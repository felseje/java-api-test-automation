package io.github.felseje.apitestautomation.core.client;

import io.github.felseje.apitestautomation.core.request.RequestContext;
import io.github.felseje.apitestautomation.factory.RequestSpecificationFactory;
import io.github.felseje.apitestautomation.factory.ResponseSpecificationFactory;
import io.github.felseje.apitestautomation.util.Strings;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public abstract class AbstractClient {

  protected Response get(RequestContext context, String endpoint) {
    return execute(context, Method.GET, endpoint);
  }

  protected Response post(RequestContext context, String endpoint) {
    return execute(context, Method.POST, endpoint);
  }

  protected Response put(RequestContext context, String endpoint) {
    return execute(context, Method.PUT, endpoint);
  }

  protected Response patch(RequestContext context, String endpoint) {
    return execute(context, Method.PATCH, endpoint);
  }

  protected Response delete(RequestContext context, String endpoint) {
    return execute(context, Method.DELETE, endpoint);
  }

  protected Response head(RequestContext context, String endpoint) {
    return execute(context, Method.HEAD, endpoint);
  }

  protected Response options(RequestContext context, String endpoint) {
    return execute(context, Method.OPTIONS, endpoint);
  }

  protected Response trace(RequestContext context, String endpoint) {
    return execute(context, Method.TRACE, endpoint);
  }

  private Response execute(RequestContext context, Method method, String endpoint) {
    RequestSpecification requestSpecification = RequestSpecificationFactory.getStandard();
    ResponseSpecification responseSpecification = ResponseSpecificationFactory.getStandard();

    if (!Strings.isBlank(context.getBaseUrl())) {
      requestSpecification.baseUri(context.getBaseUrl());
    }

    if (!context.getHeaders().isEmpty()) {
      requestSpecification.headers(context.getHeaders());
    }

    if (!context.getPathParams().isEmpty()) {
      requestSpecification.pathParams(context.getPathParams());
    }

    if (!context.getQueryParams().isEmpty()) {
      requestSpecification.queryParams(context.getQueryParams());
    }

    if (!context.getMultipartParts().isEmpty()) {
      context.getMultipartParts().forEach(requestSpecification::multiPart);
    } else if (!context.getFormParams().isEmpty()) {
      context.getFormParams().forEach(requestSpecification::formParam);
      if (context.getContentType() != null) {
        requestSpecification.contentType(context.getContentType());
      } else {
        requestSpecification.contentType(ContentType.URLENC);
      }
    } else if (context.getBody() != null) {
      requestSpecification.body(context.getBody());
      if (context.getContentType() != null) {
        requestSpecification.contentType(context.getContentType());
      } else {
        requestSpecification.contentType(ContentType.JSON);
      }
    } else {
      requestSpecification.noContentType();
    }

    return RestAssured.given(requestSpecification, responseSpecification)
        .request(method, endpoint)
        .thenReturn();
  }

}
