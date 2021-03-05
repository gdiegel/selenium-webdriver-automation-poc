package com.tektonlabs.qa;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class ShopPage extends Page {

    private static final Logger LOG = LoggerFactory.getLogger(ShopPage.class);

    public ShopPage(WebDriver driver) {
        super(driver);
        LOG.info("At {} [{}]", driver.getTitle(), driver.getCurrentUrl());
        assertThat(shopNowButton.isDisplayed()).isTrue();
    }

    @FindBy(linkText = "Shop Now")
    private WebElement shopNowButton;

    @FindBy(className = "copyright")
    private WebElement copyright;

    public WebElement getCopyright() {
        return copyright;
    }
}
