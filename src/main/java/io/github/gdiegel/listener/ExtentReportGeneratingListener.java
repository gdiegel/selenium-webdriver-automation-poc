package io.github.gdiegel.listener;

import static com.aventstack.extentreports.Status.INFO;
import static com.aventstack.extentreports.Status.WARNING;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.platform.commons.PreconditionViolationException;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;

public class ExtentReportGeneratingListener implements TestExecutionListener {

  private static final Map<TestIdentifier, TestExecutionResult> RESULTS = new HashMap<>();
  private static final Map<TestIdentifier, String> SKIPPED = new HashMap<>();
  private final ExtentSparkReporter reporter = new ExtentSparkReporter("report.html");
  private final ExtentReports extentReport = new ExtentReports();

  @Override
  public void testPlanExecutionStarted(TestPlan testPlan) {
    this.extentReport.attachReporter(reporter);
    testPlan.getChildren(getRoot(testPlan))
        .forEach(testIdentifier -> RESULTS.put(testIdentifier, null));
  }

  @Override
  public void testPlanExecutionFinished(TestPlan testPlan) {
    testPlan.getChildren(getRoot(testPlan)).forEach(klass -> {
      if (SKIPPED.containsKey(klass)) {
        extentReport.createTest(getKlassName(klass.getUniqueId())).skip(SKIPPED.get(klass));
      } else if (RESULTS.containsKey(klass)) {
        final ExtentTest testKlass = extentReport.createTest(getKlassName(klass.getUniqueId()));
        testPlan.getDescendants(klass).forEach(test -> processTestNode(testKlass, test));
      }
    });
    extentReport.flush();
  }

  private void processTestNode(ExtentTest testKlass, TestIdentifier test) {
    final ExtentTest node = testKlass.createNode(test.getDisplayName());
    if (SKIPPED.containsKey(test)) {
      node.skip(SKIPPED.get(test));
      return;
    }
    final TestExecutionResult testResult = RESULTS.get(test);
    if (testResult == null) {
      node.log(INFO, "No test results found");
      return;
    }
    switch (testResult.getStatus()) {
      case SUCCESSFUL -> node.pass(testResult.toString());
      case ABORTED -> node.log(WARNING, testResult.toString());
      case FAILED -> node.fail(testResult.toString());
      default -> throw new PreconditionViolationException(
          "Unsupported execution status:" + testResult.getStatus());
    }
  }

  @Override
  public void dynamicTestRegistered(TestIdentifier testIdentifier) {
  }

  @Override
  public void executionSkipped(TestIdentifier testIdentifier, String reason) {
    SKIPPED.put(testIdentifier, reason);
  }

  @Override
  public void executionStarted(TestIdentifier testIdentifier) {
  }

  @Override
  public void executionFinished(TestIdentifier testIdentifier,
      TestExecutionResult testExecutionResult) {
    RESULTS.put(testIdentifier, testExecutionResult);
  }

  private String getKlassName(String uniqueId) {
    return getKlassName(UniqueId.parse(uniqueId));
  }

  private String getKlassName(UniqueId uniqueId) {
    return uniqueId.getSegments().stream()
        .filter(segment -> segment.getType().equals("class"))
        .map(UniqueId.Segment::getValue)
        .collect(Collectors.joining());
  }

  private TestIdentifier getRoot(TestPlan testPlan) {
    return testPlan.getRoots().stream().findFirst().orElseThrow();
  }
}
