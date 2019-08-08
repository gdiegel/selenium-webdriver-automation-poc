package com.unitedinternet.mam.qa.specification;

import com.google.common.base.Stopwatch;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;

import static com.google.common.base.Stopwatch.createStarted;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

public class Runner {

    public static void main(String[] args) {
        final Stopwatch stopWatch = createStarted();
        final LauncherDiscoveryRequestBuilder builder = LauncherDiscoveryRequestBuilder.request();
        builder.selectors(selectClass(SearchTest.class));
        LauncherFactory.create().execute(builder.build());
        stopWatch.stop();
    }
}
