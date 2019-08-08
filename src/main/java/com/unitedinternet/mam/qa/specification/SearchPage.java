package com.unitedinternet.mam.qa.specification;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.assertj.core.api.Assertions.assertThat;

public class SearchPage extends Page {

    public SearchPage(WebDriver driver) {
        super(driver);
        assertThat(searchBar.isDisplayed()).isTrue();
    }

    @FindBy(css = ".pos-search-field__input")
    private WebElement searchBar;

    public void search(String searchTerm) {
        searchBar.sendKeys(searchTerm);
        searchBar.submit();
    }
}
