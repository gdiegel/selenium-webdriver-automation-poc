package com.unitedinternet.mam.qa.specification;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

class SearchTest extends BaseTest {

    private static final String SEARCH_TERM = "Oceans of slumber";

    @Test
    void should() {
        driver.get(BASE_URI);
        final SearchPage searchPage = new SearchPage(driver);
        searchPage.search(SEARCH_TERM);
        final WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until((ExpectedConditions.titleContains(SEARCH_TERM)));
        snap(driver);
    }

    static void snap(WebDriver webdriver) {
        final TakesScreenshot scrShot = ((TakesScreenshot) webdriver);
        final File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
        final File DestFile = new File(String.format("./snaps/%s.png", UUID.randomUUID()));
        try {
            FileUtils.copyFile(SrcFile, DestFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

