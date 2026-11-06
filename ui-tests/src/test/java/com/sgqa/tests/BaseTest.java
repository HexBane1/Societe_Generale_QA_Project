package com.sgqa.tests;

import com.sgqa.pages.CookieBanner;
import com.sgqa.pages.HomePage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Every test class extends this one, so each test gets a new Chrome browser
 * before it starts and the browser is closed after it ends.
 */
public abstract class BaseTest {

    protected WebDriver driver;

    @BeforeEach
    void startBrowser() {
        ChromeOptions options = new ChromeOptions();
        // The share price and language switch only show on a desktop-sized window
        options.addArguments("--window-size=1920,1080");

        // Chrome always runs with a normal window, not headless: the cookie banner does not appear
        // in headless Chrome. In CI there is no real screen, so the workflow gives Chrome a virtual one (xvfb-run).
        driver = new ChromeDriver(options);
    }

    @AfterEach
    void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * The precondition for UI 2 to UI 5 and INT 1: open the homepage and decline the cookie banner,
     * so the banner does not cover the page.
     */
    protected void openHomePageWithCookiesDeclined(String language) {
        new HomePage(driver).open(language);

        CookieBanner cookieBanner = new CookieBanner(driver);
        cookieBanner.declineAll();
        if (!cookieBanner.isClosed()) {
            throw new IllegalStateException("Precondition failed: the cookie banner did not close");
        }
    }
}
