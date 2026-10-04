package io.github.felseje.apitestautomation.core.request;

import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class RequestContext {

  private final String baseUrl;
  private final Map<String, Object> headers;
  private final Map<String, Object> queryParams;
  private final Map<String, Object> pathParams;
  private final Map<String, Object> multipartParts;
  private final Map<String, Object> formParams;
  private final Object body;
  private final ContentType contentType;

  private RequestContext(RequestContextBuilder builder) {
    this.baseUrl = builder.baseUrl;
    this.contentType = builder.contentType;
    this.headers = Map.copyOf(builder.headers);
    this.pathParams = Map.copyOf(builder.pathParams);
    this.queryParams = Map.copyOf(builder.queryParams);
    this.multipartParts = Map.copyOf(builder.multipartParts);
    this.formParams = Map.copyOf(builder.formParams);
    this.body = builder.body;
  }

  public static RequestContextBuilder builder() {
    return new RequestContextBuilder();
  }

  public static class RequestContextBuilder {

    private final Map<String, Object> headers = new HashMap<>();
    private final Map<String, Object> queryParams = new HashMap<>();
    private final Map<String, Object> pathParams = new HashMap<>();
    private final Map<String, Object> multipartParts = new HashMap<>();
    private final Map<String, Object> formParams = new HashMap<>();
    private String baseUrl;
    private ContentType contentType;
    private Object body;

    public RequestContextBuilder baseUrl(String baseUrl) {
      this.baseUrl = baseUrl;
      return this;
    }

    public RequestContextBuilder header(String key, Object value) {
      headers.put(key, value);
      return this;
    }

    public RequestContextBuilder queryParam(String key, Object value) {
      queryParams.put(key, value);
      return this;
    }

    public RequestContextBuilder pathParam(String key, Object value) {
      pathParams.put(key, value);
      return this;
    }

    public RequestContextBuilder multipartPart(String key, Object value) {
      multipartParts.put(key, value);
      return this;
    }

    public RequestContextBuilder formParam(String key, Object value) {
      formParams.put(key, value);
      return this;
    }

    public RequestContextBuilder body(Object body) {
      this.body = body;
      return this;
    }

    public RequestContextBuilder contentType(ContentType contentType) {
      this.contentType = contentType;
      return this;
    }

    public RequestContext build() {
      return new RequestContext(this);
    }

  }

}
