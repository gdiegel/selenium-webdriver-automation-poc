package com.tektonlabs.qa;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

import static org.assertj.core.api.Assertions.assertThat;

class ShopTest extends BaseTest {

    @Test
    void shouldBeAbleToGoToShop() {
        final ShopPage shopPage = new MainPage(driver).goToShop();
        final WebElement copyright = shopPage.getCopyright();
        scrollTo(copyright);
        assertThat(copyright.getText()).isEqualTo("© 2021. All rights reserved.");
        snap(driver);
    }
}

