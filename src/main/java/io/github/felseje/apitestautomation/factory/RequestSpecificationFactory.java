package io.github.felseje.apitestautomation.factory;

import io.github.felseje.apitestautomation.config.Config;
import io.github.felseje.apitestautomation.config.ConfigurationManager;
import io.github.felseje.apitestautomation.util.Strings;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.specification.RequestSpecification;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RequestSpecificationFactory {

  public static RequestSpecification getStandard() {
    Config config = ConfigurationManager.getConfig();
    ObjectMapperConfig objMapperCfg = ObjectMapperConfig.objectMapperConfig()
        .defaultObjectMapperType(ObjectMapperType.GSON);
    RestAssuredConfig restAssuredConfig = new RestAssuredConfig()
        .httpClient(getHttpClientConfig(config))
        .objectMapperConfig(objMapperCfg);
    RequestSpecBuilder builder = new RequestSpecBuilder()
        .setConfig(restAssuredConfig)
        .setRelaxedHTTPSValidation()
        .log(LogDetail.METHOD)
        .log(LogDetail.URI)
        .log(LogDetail.HEADERS)
        .log(LogDetail.PARAMS)
        .log(LogDetail.BODY);
    if (!Strings.isBlank(config.baseUrl())) {
      builder.setBaseUri(config.baseUrl());
    }
    return builder.build();
  }

  private static HttpClientConfig getHttpClientConfig(Config config) {
    return new HttpClientConfig()
        .setParam("http.socket.timeout", config.httpSocketTimeout())
        .setParam("http.connect.timeout", config.httpConnectTimeout())
        .setParam("http.connection-manager.timeout", config.httpConnectionManagerTimeout());
  }

}
