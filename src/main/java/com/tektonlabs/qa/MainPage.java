package com.tektonlabs.qa;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class MainPage extends Page {

    private static final Logger LOG = LoggerFactory.getLogger(MainPage.class);

    public MainPage(WebDriver driver) {
        super(driver);
        LOG.info("At {} [{}]", driver.getTitle(), driver.getCurrentUrl());
        assertThat(mainNavigation.isDisplayed()).isTrue();
    }

    @FindBy(id = "menu-main-nav")
    private WebElement mainNavigation;

    @FindBy(id = "menu-item-4460")
    private WebElement shopLink;

    public ShopPage goToShop() {
        shopLink.click();
        return new ShopPage(driver);
    }
}
