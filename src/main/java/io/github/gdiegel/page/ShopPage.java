package io.github.gdiegel.page;

import static org.assertj.core.api.Assertions.assertThat;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ShopPage extends Page {

  public ShopPage(WebDriver driver) {
    super(driver);
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
