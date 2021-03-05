package com.tektonlabs.qa;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

class ShopTest extends BaseTest {

    @Test
    void shouldBeAbleToGoToShop() {
        final ShopPage shopPage = new MainPage(driver).goToShop();
        final WebElement copyright = shopPage.getCopyright();
        Assertions.assertThat(copyright.getText()).isEqualTo("© 2021. All rights reserved.");
        snap(driver);
    }
}

