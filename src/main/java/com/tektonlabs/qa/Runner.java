package com.tektonlabs.qa;

import com.google.common.base.Stopwatch;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.google.common.base.Stopwatch.createStarted;
import static java.lang.String.format;
import static java.lang.System.exit;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

public class Runner {

    private static final Logger LOG = LoggerFactory.getLogger(Runner.class);

    public static void main(String[] args) {
        final Stopwatch stopWatch = createStarted();
        final LauncherDiscoveryRequestBuilder builder = LauncherDiscoveryRequestBuilder.request();
        builder.selectors(selectClass(ShopTest.class));
        final SummaryGeneratingListener listener = new SummaryGeneratingListener();
        LauncherFactory.create().execute(builder.build(), listener);
        stopWatch.stop();
        final List<TestExecutionSummary.Failure> failures = listener.getSummary().getFailures();
        LOG.info(format("Finished test suite in [%s] with [%s] failures", stopWatch, failures.size()));
        failures.forEach(failure -> LOG.info(format("%s: %s", failure.getTestIdentifier().getDisplayName(), failure.getException())));
        exit(failures.size() == 0 ? 0 : 1);
    }
}
