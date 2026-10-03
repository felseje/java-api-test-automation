package io.github.felseje.apitestautomation.core.response;

import static org.apache.http.HttpStatus.SC_ACCEPTED;
import static org.apache.http.HttpStatus.SC_BAD_GATEWAY;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_GATEWAY_TIMEOUT;
import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_SERVICE_UNAVAILABLE;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.apache.http.HttpStatus.SC_UNPROCESSABLE_ENTITY;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasXPath;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.matchesPattern;

import io.github.felseje.apitestautomation.util.ArgumentValidator;
import io.restassured.common.mapper.TypeRef;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import java.util.List;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.hamcrest.Matcher;
import org.opentest4j.TestAbortedException;

@RequiredArgsConstructor
public class ApiResponse {

  private static final int SC_TOO_MANY_REQUESTS = 429;
  private final Response response;

  public ApiResponse assumeThat(Consumer<ApiResponse> consumer, String message) {
    ArgumentValidator.requireNotNull(consumer, "Consumer cannot be null");
    ArgumentValidator.requireNotBlank(message, "Message cannot be blank");

    try {
      consumer.accept(this);
    } catch (AssertionError e) {
      throw new TestAbortedException(message);
    }
    return this;
  }

  public ApiResponse assumeStatusCode(int statusCode) {
    return assumeThat(
        consumer -> consumer.shouldHaveStatus(statusCode),
        "The status code is not equal to expected"
    );
  }

  public ApiResponse shouldHaveStatus(int expectedStatusCode) {
    response.then()
        .assertThat()
        .statusCode(expectedStatusCode);
    return this;
  }

  public ApiResponse shouldBeOk() {
    return shouldHaveStatus(SC_OK);
  }

  public ApiResponse shouldBeCreated() {
    return shouldHaveStatus(SC_CREATED);
  }

  public ApiResponse shouldBeAccepted() {
    return shouldHaveStatus(SC_ACCEPTED);
  }

  public ApiResponse shouldBeNoContent() {
    return shouldHaveStatus(SC_NO_CONTENT);
  }

  public ApiResponse shouldBeBadRequest() {
    return shouldHaveStatus(SC_BAD_REQUEST);
  }

  public ApiResponse shouldBeUnauthorized() {
    return shouldHaveStatus(SC_UNAUTHORIZED);
  }

  public ApiResponse shouldBeForbidden() {
    return shouldHaveStatus(SC_FORBIDDEN);
  }

  public ApiResponse shouldBeNotFound() {
    return shouldHaveStatus(SC_NOT_FOUND);
  }

  public ApiResponse shouldBeUnprocessableEntity() {
    return shouldHaveStatus(SC_UNPROCESSABLE_ENTITY);
  }

  public ApiResponse shouldBeTooManyRequests() {
    return shouldHaveStatus(SC_TOO_MANY_REQUESTS);
  }

  public ApiResponse shouldBeInternalServerError() {
    return shouldHaveStatus(SC_INTERNAL_SERVER_ERROR);
  }

  public ApiResponse shouldBeBadGateway() {
    return shouldHaveStatus(SC_BAD_GATEWAY);
  }

  public ApiResponse shouldBeServiceUnavailable() {
    return shouldHaveStatus(SC_SERVICE_UNAVAILABLE);
  }

  public ApiResponse shouldBeGatewayTimeout() {
    return shouldHaveStatus(SC_GATEWAY_TIMEOUT);
  }

  public ApiResponse shouldHaveJsonField(String path, Object value) {
    return assertJsonField(path, equalTo(value));

  }

  public <T extends Comparable<T>> ApiResponse shouldHaveJsonFieldGreaterThan(
      String path,
      T value
  ) {
    return assertJsonField(path, greaterThan(value));
  }

  public <T extends Comparable<T>> ApiResponse shouldHaveJsonFieldLessThan(
      String path,
      T value
  ) {
    return assertJsonField(path, lessThan(value));
  }

  public <T extends Comparable<T>> ApiResponse shouldHaveJsonFieldBetweenExclusive(
      String path,
      T minValue,
      T maxValue
  ) {
    return assertJsonField(path, allOf(
        greaterThan(minValue),
        lessThan(maxValue)
    ));
  }

  public <T extends Comparable<T>> ApiResponse shouldHaveJsonFieldBetweenInclusive(
      String path,
      T minValue,
      T maxValue
  ) {
    return assertJsonField(path, allOf(
        greaterThanOrEqualTo(minValue),
        lessThanOrEqualTo(maxValue)
    ));
  }

  public ApiResponse shouldContainJsonField(String path) {
    final int lastDot = path.lastIndexOf('.');
    final String parentPath = lastDot > 0 ? path.substring(0, lastDot) : "$";
    final String key = path.substring(lastDot + 1);
    return assertJsonField(parentPath, hasKey(key));
  }

  public ApiResponse shouldHaveHeader(String headerName, String expectedValue) {
    ArgumentValidator.requireNotBlank(expectedValue, "Expected header value cannot be blank");
    return assertHeader(headerName, equalTo(expectedValue));
  }

  public ApiResponse shouldHaveHeaderContaining(String headerName, String expectedSubstring) {
    ArgumentValidator.requireNotBlank(expectedSubstring, "Expected substring cannot be blank");
    return assertHeader(headerName, containsString(expectedSubstring));
  }

  public ApiResponse shouldHaveHeaderMatching(String headerName, String regex) {
    ArgumentValidator.requireNotBlank(regex, "Regex cannot be blank");
    return assertHeader(headerName, matchesPattern(regex));
  }

  public ApiResponse shouldHaveXmlField(String xpath, String value) {
    ArgumentValidator.requireNotBlank(xpath, "Xml path cannot be blank");
    response.then()
        .assertThat()
        .body(hasXPath(xpath, equalTo(value)));
    return this;
  }

  public ApiResponse shouldMatchJsonSchema(String schemaJson) {
    ArgumentValidator.requireNotBlank(schemaJson, "Schema JSON cannot be blank");
    response.then()
        .assertThat()
        .body(JsonSchemaValidator.matchesJsonSchema(schemaJson));
    return this;
  }

  public ApiResponse shouldMatchJsonSchemaInClasspath(String classpathResource) {
    ArgumentValidator.requireNotBlank(classpathResource, "Classpath resource cannot be blank");
    response.then()
        .assertThat()
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(classpathResource));
    return this;
  }

  public <T> T getJsonFieldValue(String path) {
    shouldContainJsonField(path);
    return response.then()
        .extract()
        .path(path);
  }

  public boolean isInformational() {
    return isStatusCodeInRange(100, 200);
  }

  public boolean isSuccess() {
    return isStatusCodeInRange(200, 300);
  }

  public boolean isRedirection() {
    return isStatusCodeInRange(300, 400);
  }

  public boolean isClientError() {
    return isStatusCodeInRange(400, 500);
  }

  public boolean isServerError() {
    return isStatusCodeInRange(500, 600);
  }

  public boolean isStatusCodeInRange(int fromInclusive, int toExclusive) {
    int statusCode = response.getStatusCode();
    return statusCode >= fromInclusive && statusCode < toExclusive;
  }

  public Response raw() {
    return response;
  }

  public <T> T as(Class<T> clazz) {
    return response.as(clazz);
  }

  public <T> List<T> asList(Class<T> clazz) {
    return response.as(new TypeRef<List<T>>() {
    });
  }

  private ApiResponse assertJsonField(String path, Matcher<?> matcher) {
    ArgumentValidator.requireNotBlank(path, "Json path cannot be blank");
    response.then()
        .assertThat()
        .body(path, matcher);
    return this;
  }

  private ApiResponse assertHeader(String headerName, Matcher<?> matcher) {
    ArgumentValidator.requireNotBlank(headerName, "Header name cannot be blank");
    response.then()
        .assertThat()
        .header(headerName, matcher);
    return this;
  }
}
