package io.github.gdiegel;

import io.github.gdiegel.page.MainPage;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ShopTest extends BaseTest {

    @Test
    void shouldBeAbleToGoToShop() {
        final var shopPage = new MainPage(driver).goToShop();
        final var copyright = shopPage.getCopyright();
        scrollTo(copyright);
        assertThat(copyright.getText()).isEqualTo("© 2021. All rights reserved.");
        snap(driver);
    }
}

