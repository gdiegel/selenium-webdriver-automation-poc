package io.github.gdiegel;

import static com.google.common.base.Stopwatch.createStarted;
import static java.lang.String.format;
import static java.lang.System.exit;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

import com.google.common.base.Stopwatch;
import io.github.gdiegel.listener.ExtentReportGeneratingListener;
import io.github.gdiegel.test.ShopTest;
import java.util.List;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary.Failure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Runner {

  private static final Logger LOG = LoggerFactory.getLogger(Runner.class);

  public static void main(String[] args) {
    final Stopwatch stopWatch = createStarted();
    final LauncherDiscoveryRequestBuilder builder = LauncherDiscoveryRequestBuilder.request();
    builder.selectors(selectClass(ShopTest.class));
    final SummaryGeneratingListener summaryGeneratingListener = new SummaryGeneratingListener();
    final ExtentReportGeneratingListener extentReportGeneratingListener = new ExtentReportGeneratingListener();
    LauncherFactory.create()
        .execute(builder.build(), summaryGeneratingListener, extentReportGeneratingListener);
    stopWatch.stop();
    final List<Failure> failures = summaryGeneratingListener.getSummary().getFailures();
    LOG.info(format("Finished test suite in [%s] with [%s] failures", stopWatch, failures.size()));
    failures.forEach(failure -> LOG.info(
        format("%s: %s", failure.getTestIdentifier().getDisplayName(), failure.getException())));
    exit(failures.size() == 0 ? 0 : 1);
  }
}
