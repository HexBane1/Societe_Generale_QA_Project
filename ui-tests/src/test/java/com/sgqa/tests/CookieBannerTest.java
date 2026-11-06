package com.sgqa.tests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sgqa.pages.CookieBanner;
import com.sgqa.pages.HomePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CookieBannerTest extends BaseTest {

    @Test
    @DisplayName("UI 1: Cookie banner can be declined")
    void cookieBannerCanBeDeclined() {
        HomePage homePage = new HomePage(driver);
        CookieBanner cookieBanner = new CookieBanner(driver);

        homePage.open("en");
        cookieBanner.declineAll();

        assertTrue(cookieBanner.isClosed(), "The cookie banner should close after clicking \"Decline all\"");
    }
}
