package io.github.gdiegel;

import io.github.gdiegel.listener.ExtentReportGeneratingListener;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Stopwatch.createStarted;
import static java.lang.String.format;
import static java.lang.System.exit;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

public class Runner {

    private static final Logger LOG = LoggerFactory.getLogger(Runner.class);

    public static void main(String[] args) {
        final var stopWatch = createStarted();
        final var builder = LauncherDiscoveryRequestBuilder.request();
        builder.selectors(selectClass(ShopTest.class));
        final var summaryGeneratingListener = new SummaryGeneratingListener();
        final var extentReportGeneratingListener = new ExtentReportGeneratingListener();
        LauncherFactory.create().execute(builder.build(), summaryGeneratingListener, extentReportGeneratingListener);
        stopWatch.stop();
        final var failures = summaryGeneratingListener.getSummary().getFailures();
        LOG.info(format("Finished test suite in [%s] with [%s] failures", stopWatch, failures.size()));
        failures.forEach(failure -> LOG.info(format("%s: %s", failure.getTestIdentifier().getDisplayName(), failure.getException())));
        exit(failures.size() == 0 ? 0 : 1);
    }
}
