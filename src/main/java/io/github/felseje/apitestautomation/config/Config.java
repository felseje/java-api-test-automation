package io.github.felseje.apitestautomation.config;

import org.aeonbits.owner.Config.Sources;

@Sources({"classpath:config/${env}.properties"})
public interface Config extends org.aeonbits.owner.Config {

  @Key("base.url")
  String baseUrl();

  @Key("http.connect.timeout")
  @DefaultValue("5000")
  int httpConnectTimeout();

  @Key("http.socket.timeout")
  @DefaultValue("10000")
  int httpSocketTimeout();

  @Key("http.connection-manager.timeout")
  @DefaultValue("10000")
  int httpConnectionManagerTimeout();
}
