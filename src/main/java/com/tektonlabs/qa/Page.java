package com.tektonlabs.qa;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Page {

    private static final Logger LOG = LoggerFactory.getLogger(Page.class);

    protected WebDriver driver;

    public Page(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        LOG.info("At [{}] => [{}]", driver.getTitle(), driver.getCurrentUrl());
    }
}
