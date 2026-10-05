package io.github.felseje.apitestautomation.test;

import java.lang.reflect.Method;
import java.util.Deque;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedDeque;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.TestInfo;

@Slf4j
public abstract class BaseIT {

  private final Deque<CleanupAction> cleanupActions = new ConcurrentLinkedDeque<>();

  protected void scheduleCleanup(
      String resource,
      String identifier,
      Runnable action
  ) {

    cleanupActions.addLast(
        new CleanupAction(
            resource,
            identifier,
            Objects.requireNonNull(action, "Cleanup action must not be null")
        )
    );
  }

  @AfterEach
  void cleanup(TestInfo testInfo) {
    CleanupAction cleanup;
    String testName = testInfo.getTestClass()
        .map(Class::getSimpleName)
        .orElse("UnknownClass")
        + "."
        + testInfo.getTestMethod()
        .map(Method::getName)
        .orElse("UnknownMethod");

    while ((cleanup = cleanupActions.pollLast()) != null) {
      try {
        cleanup.action().run();
      } catch (Exception | AssertionError e) {
        log.warn(
            "Failed to cleanup resource: type={}, id={}, test={}",
            cleanup.resource(),
            cleanup.identifier(),
            testName,
            e
        );
      }
    }
  }

  protected record CleanupAction(
      String resource,
      String identifier,
      Runnable action
  ) {

  }
}
