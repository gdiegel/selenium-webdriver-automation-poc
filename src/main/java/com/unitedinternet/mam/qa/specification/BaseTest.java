package com.unitedinternet.mam.qa.specification;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class BaseTest {

    private static final Browser BROWSER = Browser.valueOf(env("BROWSER").orElse("chrome").toUpperCase());
    static final String BASE_URI = env("BASE_URI").orElse("https://suche.web.de/web");
    WebDriver driver;

    @BeforeAll
    void init() {
        driver = getBrowser();
    }

    @AfterAll
    void tearDown() {
        driver.quit();
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
        System.out.printf("Capabilities of [%s]:%n", BROWSER);
        ((HasCapabilities) driver).getCapabilities().asMap().forEach((key, value) -> System.out.println(key + ":" + value));
        return driver;
    }

    private static ChromeOptions configureChrome() {
        final ChromeOptions options = new ChromeOptions();
        options.setAcceptInsecureCerts(true);
        options.setHeadless(true);
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage");
        return options;
    }

    private static FirefoxOptions configureFirefox() {
        final FirefoxOptions options = new FirefoxOptions();
        options.setAcceptInsecureCerts(true);
        options.setHeadless(true);
        return options;
    }

}

