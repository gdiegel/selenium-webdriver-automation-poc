package io.github.gdiegel.listener;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.LongAdder;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.apache.commons.io.FileUtils.copyFile;
import static org.apache.commons.io.FileUtils.copyToFile;

public class SnapListener extends AbstractWebDriverEventListener {

    private static final Logger LOG = LoggerFactory.getLogger(SnapListener.class);
    private static final SnapListener INSTANCE = new SnapListener();
    private static final LocalDateTime now = LocalDateTime.now();
    private ExtensionContext context;
    private final LongAdder longAdder = new LongAdder();

    private SnapListener() {
    }

    public static SnapListener getInstance() {
        return INSTANCE;
    }

    public ExtensionContext getContext() {
        return context;
    }

    public LongAdder getLongAdder() {
        return longAdder;
    }

    public void setContext(ExtensionContext context) {
        this.context = context;
        getLongAdder().sumThenReset();
    }

    private String getCurrentTestPath() {
        return format("%s.%s.%03d", getContext().getRequiredTestClass().getSimpleName(), getContext().getRequiredTestMethod().getName(), getLongAdder().intValue());
    }

    @Override
    public void beforeFindBy(By by, WebElement element, WebDriver driver) {
        getLongAdder().increment();
        LOG.debug("[{}] [{}] beforeFindBy {}", getCurrentTestPath(), driver.getCurrentUrl(), by.toString());
        saveScreenshot(driver, getCurrentTestPath());
        savePageSource(driver, getCurrentTestPath());
    }

    @Override
    public void beforeClickOn(WebElement element, WebDriver driver) {
        getLongAdder().increment();
        LOG.debug("[{}] [{}] beforeClickOn {}", getCurrentTestPath(), driver.getCurrentUrl(), element.toString());
        saveScreenshot(driver, getCurrentTestPath());
        savePageSource(driver, getCurrentTestPath());
    }

    @Override
    public void afterClickOn(WebElement element, WebDriver driver) {
        getLongAdder().increment();
        LOG.debug("[{}] [{}] afterClickOn {}", getCurrentTestPath(), driver.getCurrentUrl(), element.toString());
        saveScreenshot(driver, getCurrentTestPath());
        savePageSource(driver, getCurrentTestPath());
    }

    @Override
    public void beforeChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
        getLongAdder().increment();
        LOG.debug("[{}] [{}] beforeChangeValueOf {} to {}", getCurrentTestPath(), driver.getCurrentUrl(), element.toString(), keysToSend);
        saveScreenshot(driver, getCurrentTestPath());
        savePageSource(driver, getCurrentTestPath());
    }

    @Override
    public void afterChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
        getLongAdder().increment();
        LOG.debug("[{}] [{}] afterChangeValueOf {} to {}", getCurrentTestPath(), driver.getCurrentUrl(), element.toString(), keysToSend);
        saveScreenshot(driver, getCurrentTestPath());
        savePageSource(driver, getCurrentTestPath());
    }

    @Override
    public void onException(Throwable throwable, WebDriver driver) {
        getLongAdder().increment();
        LOG.debug("[{}] [{}] onException", getCurrentTestPath(), driver.getCurrentUrl(), throwable);
        saveScreenshot(driver, getCurrentTestPath());
        savePageSource(driver, getCurrentTestPath());
    }

    private void saveScreenshot(WebDriver webdriver, String identifier) {
        final var srcFile = ((TakesScreenshot) webdriver).getScreenshotAs(OutputType.FILE);
        final var destFile = new File(format("./snaps/%s/%s.png", now.format(ofPattern("yyyyMMdd'T'HHmm")), identifier));
        try {
            copyFile(srcFile, destFile);
        } catch (IOException e) {
            LOG.error("Couldn't take screenshot", e);
        }
    }

    private void savePageSource(WebDriver webdriver, String identifier) {
        final var srcFile = webdriver.getPageSource();
        final var destFile = new File(format("./snaps/%s/%s.html", now.format(ofPattern("yyyyMMdd'T'HHmm")), identifier));
        try (final var bais = new ByteArrayInputStream(srcFile.getBytes(UTF_8))) {
            copyToFile(bais, destFile);
        } catch (IOException e) {
            LOG.error("Couldn't save page source", e);
        }
    }
}

