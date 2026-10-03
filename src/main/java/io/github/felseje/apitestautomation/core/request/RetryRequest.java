package io.github.felseje.apitestautomation.core.request;

import io.github.felseje.apitestautomation.core.response.ApiResponse;
import io.github.felseje.apitestautomation.util.ArgumentValidator;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.awaitility.Awaitility;

public class RetryRequest {

  private static final String NULL_TIME_UNIT_ERROR = "The 'unit' cannot be null";

  private final Supplier<ApiResponse> action;
  private Duration timeout = Duration.ofSeconds(10);
  private Duration poll = Duration.ofMillis(500);
  private RetryStrategy strategy = RetryStrategy.AT_MOST;
  private Duration minDuration;
  private Duration maxDuration;

  public RetryRequest(Supplier<ApiResponse> action) {
    ArgumentValidator.requireNotNull(action, "The 'action' cannot be null");
    this.action = action;
  }

  public RetryRequest atMost(long time, TimeUnit unit) {
    ArgumentValidator.requireNotNull(unit, NULL_TIME_UNIT_ERROR);
    this.strategy = RetryStrategy.AT_MOST;
    this.timeout = Duration.ofMillis(unit.toMillis(time));
    return this;
  }

  public RetryRequest atLeast(long time, TimeUnit unit) {
    ArgumentValidator.requireNotNull(unit, NULL_TIME_UNIT_ERROR);
    this.strategy = RetryStrategy.AT_LEAST;
    this.minDuration = Duration.ofMillis(unit.toMillis(time));
    this.timeout = Duration.ofMillis(unit.toMillis(time));
    return this;
  }

  public RetryRequest between(long minTime, long maxTime, TimeUnit unit) {
    ArgumentValidator.requireNotNull(unit, NULL_TIME_UNIT_ERROR);
    this.strategy = RetryStrategy.BETWEEN;
    this.minDuration = Duration.ofMillis(unit.toMillis(minTime));
    this.maxDuration = Duration.ofMillis(unit.toMillis(maxTime));
    return this;
  }

  public RetryRequest pollEvery(long time, TimeUnit unit) {
    ArgumentValidator.requireNotNull(unit, "The 'unit' cannot be null");
    this.poll = Duration.ofMillis(unit.toMillis(time));
    return this;
  }

  public ApiResponse until(Predicate<ApiResponse> condition) {
    ArgumentValidator.requireNotNull(condition, "The 'condition' cannot be null");
    AtomicReference<ApiResponse> last = new AtomicReference<>();
    switch (strategy) {
      case AT_MOST -> Awaitility.await()
          .atMost(timeout)
          .pollInterval(poll)
          .ignoreExceptions()
          .until(() -> {
            ApiResponse response = action.get();
            last.set(response);
            return condition.test(response);
          });
      case AT_LEAST -> {
        Awaitility.await()
            .atLeast(minDuration)
            .pollInterval(poll)
            .ignoreExceptions()
            .until(() -> {
              ApiResponse response = action.get();
              last.set(response);
              return condition.test(response);
            });
      }
      case BETWEEN -> Awaitility.await()
          .atLeast(minDuration)
          .atMost(maxDuration)
          .pollInterval(poll)
          .ignoreExceptions()
          .until(() -> {
            ApiResponse response = action.get();
            last.set(response);
            return condition.test(response);
          });
    }
    return last.get();
  }

}
