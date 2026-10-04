package io.github.felseje.apitestautomation.factory;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.ResponseSpecification;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ResponseSpecificationFactory {

  public static ResponseSpecification getStandard() {
    return new ResponseSpecBuilder()
        .log(LogDetail.ALL)
        .build();
  }

}
