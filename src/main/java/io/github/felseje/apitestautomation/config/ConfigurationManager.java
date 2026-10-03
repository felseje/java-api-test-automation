package io.github.felseje.apitestautomation.config;

import io.github.felseje.apitestautomation.exception.ConfigurationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aeonbits.owner.ConfigCache;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConfigurationManager {

  public static Config getConfig() {
    try {
      String env = System.getProperty("env", "config");
      log.info("Loading properties from environment: {}", "config".equals(env) ? "standard" : env);
      System.setProperty("env", env);
      return ConfigCache.getOrCreate(Config.class);
    } catch (Exception e) {
      throw new ConfigurationException("Failed to load configuration", e);
    }
  }

}
