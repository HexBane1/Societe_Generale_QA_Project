package com.sgqa.tests;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sgqa.pages.Header;
import com.sgqa.pages.HomePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LanguageSwitchTest extends BaseTest {

    @Test
    @DisplayName("UI 2: Switch from English to French")
    void switchFromEnglishToFrench() {
        HomePage homePage = new HomePage(driver);
        Header header = new Header(driver);

        openHomePageWithCookiesDeclined("en");
        header.switchLanguage("fr");

        String url = homePage.getUrl();
        String title = homePage.getTitle();

        // assertAll runs every check and reports all failures together, not just the first one
        assertAll(
                () -> assertTrue(url.endsWith("/fr"), "URL should end with /fr but was: " + url),
                () -> assertEquals("fr", homePage.getLanguage(), "<html lang> should be fr"),
                () -> assertTrue(title.contains("Accueil"), "Title should contain \"Accueil\" but was: " + title)
        );
    }

    @Test
    @DisplayName("UI 3: Switch from French to English")
    void switchFromFrenchToEnglish() {
        HomePage homePage = new HomePage(driver);
        Header header = new Header(driver);

        openHomePageWithCookiesDeclined("fr");
        header.switchLanguage("en");

        String url = homePage.getUrl();
        String title = homePage.getTitle();

        assertAll(
                () -> assertTrue(url.endsWith("/en"), "URL should end with /en but was: " + url),
                () -> assertEquals("en", homePage.getLanguage(), "<html lang> should be en"),
                () -> assertTrue(title.contains("Home"), "Title should contain \"Home\" but was: " + title)
        );
    }
}
