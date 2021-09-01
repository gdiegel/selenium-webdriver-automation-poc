package io.github.gdiegel;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.gdiegel.extension.SnapExtension;
import io.github.gdiegel.listener.SnapListener;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SnapExtension.class)
abstract class BaseTest {

    private static final Browser BROWSER = Browser.valueOf(env("BROWSER").orElse("firefox").toUpperCase());
    private static final Logger LOG = LoggerFactory.getLogger(BaseTest.class);
    static final String BASE_URI = env("BASE_URI").orElse("https://thermomix.com/");
    WebDriver driver;

    @BeforeAll
    void init() {
        driver = new EventFiringWebDriver(getBrowser());
        ((EventFiringWebDriver) driver).register(SnapListener.getInstance());
    }

    @BeforeEach
    void setup() {
        driver.get(BASE_URI);
    }

    @AfterAll
    void deInit() {
        driver.quit();
    }

    protected void scrollTo(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    private static Optional<String> env(String key) {
        if (key == null) {
            return Optional.empty();
        } else {
            var var = System.getenv(key);
            if (var == null) {
                var = System.getProperty(key);
            }
            return Optional.ofNullable(var);
        }
    }

    private WebDriver getBrowser() {
        WebDriver driver = null;
        switch (BROWSER) {
            case FIREFOX -> {
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver(configureFirefox());
            }
            case CHROME -> {
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver(configureChrome());
            }
        }
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        LOG.info("Capabilities of [{}]:", BROWSER);
        ((HasCapabilities) driver).getCapabilities().asMap().forEach((key, value) -> LOG.info(key + ":" + value));
        return driver;
    }

    private static ChromeOptions configureChrome() {
        final var options = new ChromeOptions();
        options.setAcceptInsecureCerts(false);
        options.setHeadless(false);
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage");
        return options;
    }

    private static FirefoxOptions configureFirefox() {
        final var options = new FirefoxOptions();
        options.setAcceptInsecureCerts(false);
        options.setHeadless(false);
        return options;
    }

    static void snap(WebDriver webdriver) {
        final var scrShot = ((TakesScreenshot) webdriver);
        final var SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
        final var DestFile = new File(String.format("./snaps/%s.png", UUID.randomUUID()));
        try {
            FileUtils.copyFile(SrcFile, DestFile);
        } catch (IOException e) {
            LOG.debug("Couldn't take screenshot", e);
            throw new RuntimeException(e);
        }
    }

}

