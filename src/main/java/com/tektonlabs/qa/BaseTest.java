package com.tektonlabs.qa;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class BaseTest {

    private static final Browser BROWSER = Browser.valueOf(env("BROWSER").orElse("firefox").toUpperCase());
    private static final Logger LOG = LoggerFactory.getLogger(BaseTest.class);
    static final String BASE_URI = env("BASE_URI").orElse("https://thermomix.com/");
    WebDriver driver;

    @BeforeAll
    void init() {
        driver = getBrowser();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
        driver.get(BASE_URI);
    }

    @AfterAll
    void tearDown() {
        driver.quit();
    }

    protected JavascriptExecutor scroller() {
        return (JavascriptExecutor) driver;
    }

    private static Optional<String> env(String key) {
        if (key == null) {
            return Optional.empty();
        } else {
            String var = System.getenv(key);
            if (var == null) {
                var = System.getProperty(key);
            }
            return Optional.ofNullable(var);
        }
    }

    private WebDriver getBrowser() {
        WebDriver driver = null;
        switch (BROWSER) {
            case FIREFOX:
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver(configureFirefox());
                break;
            case CHROME:
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver(configureChrome());
                break;
        }
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        LOG.info("Capabilities of [{}]:", BROWSER);
        ((HasCapabilities) driver).getCapabilities().asMap().forEach((key, value) -> LOG.info(key + ":" + value));
        return driver;
    }

    private static ChromeOptions configureChrome() {
        final ChromeOptions options = new ChromeOptions();
        options.setAcceptInsecureCerts(false);
        options.setHeadless(false);
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage");
        return options;
    }

    private static FirefoxOptions configureFirefox() {
        final FirefoxOptions options = new FirefoxOptions();
        options.setAcceptInsecureCerts(false);
        options.setHeadless(false);
        return options;
    }

    static void snap(WebDriver webdriver) {
        final TakesScreenshot scrShot = ((TakesScreenshot) webdriver);
        final File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
        final File DestFile = new File(String.format("./snaps/%s.png", UUID.randomUUID()));
        try {
            FileUtils.copyFile(SrcFile, DestFile);
        } catch (IOException e) {
            LOG.debug("Couldn't take screenshot", e);
            throw new RuntimeException(e);
        }
    }

}

