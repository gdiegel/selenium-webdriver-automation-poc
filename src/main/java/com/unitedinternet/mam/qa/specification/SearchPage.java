package com.unitedinternet.mam.qa.specification;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SearchPage extends Page {

    public SearchPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(css = ".pos-search-field__input")
    private WebElement searchBar;

    public void search(String searchTerm) {
        searchBar.sendKeys(searchTerm);
        searchBar.submit();
    }
}
