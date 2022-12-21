package io.github.gdiegel.test;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.gdiegel.page.MainPage;
import io.github.gdiegel.page.ShopPage;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

public class ShopTest extends BaseTest {

  @Test
  void shouldBeAbleToGoToShop() {
    final ShopPage shopPage = new MainPage(driver).goToShop();
    final WebElement copyright = shopPage.getCopyright();
    scrollTo(copyright);
    assertThat(copyright.getText()).isEqualTo("© 2021. All rights reserved.");
    snap(driver);
  }
}

