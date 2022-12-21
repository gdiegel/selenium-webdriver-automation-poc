package io.github.gdiegel.test;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.gdiegel.page.MainPage;
import org.junit.jupiter.api.Test;

public class ShopTest extends BaseTest {

    @Test
    void shouldBeAbleToGoToShop() {
        final var shopPage = new MainPage(driver).goToShop();
        final var copyright = shopPage.getCopyright();
        scrollTo(copyright);
        assertThat(copyright.getText()).isEqualTo("© 2021. All rights reserved.");
        snap(driver);
    }
}

