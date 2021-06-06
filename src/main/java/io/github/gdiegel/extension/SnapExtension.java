package io.github.gdiegel.extension;

import io.github.gdiegel.listener.SnapListener;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SnapExtension implements BeforeEachCallback {

    private static final Logger LOG = LoggerFactory.getLogger(SnapExtension.class);

    @Override
    public void beforeEach(ExtensionContext context) {
        LOG.info("RUN [{}.{}]", context.getRequiredTestClass().getSimpleName(), context.getRequiredTestMethod().getName());
        SnapListener.getInstance().setContext(context);
    }
}
