package com.sgqa.pages;

import java.time.Duration;
import java.util.regex.Pattern;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * The header at the top of the page. The page has a desktop copy and a mobile copy
 * of the header items, so every selector starts with .header-menu-bourse (the desktop one).
 */
public class Header {

    private static final By PAGE = By.tagName("html");
    private static final By SHARE_PRICE = By.cssSelector(".header-menu-bourse .cote-bourse-price");
    private static final By SHARE_PRICE_LINK = By.cssSelector(".header-menu-bourse a.cote-bourse");

    private final WebDriver driver;
    private final WebDriverWait wait;

    public Header(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /** Clicks "Fr" or "En" in the header and waits for the new page to load. language is "fr" or "en". */
    public void switchLanguage(String language) {
        By languageLink = By.cssSelector(".header-menu-bourse .language-list a[hreflang='" + language + "']");

        // Remember the current page, so we can tell when the browser has moved to a new one
        WebElement oldPage = driver.findElement(PAGE);

        wait.until(ExpectedConditions.elementToBeClickable(languageLink)).click();

        // "Stale" means the old page is gone, so the new page has started loading
        wait.until(ExpectedConditions.stalenessOf(oldPage));
    }

    /** Waits until the share price shows a number, then returns it as text, for example "63.99". */
    public String getSharePrice() {
        // The price is filled in by the page after it loads, so wait until the text contains a digit
        wait.until(ExpectedConditions.textMatches(SHARE_PRICE, Pattern.compile("\\d")));
        return driver.findElement(SHARE_PRICE).getText().trim();
    }

    /** Returns the aria-label of the share price link: the text a screen reader reads out. */
    public String getSharePriceLabel() {
        return driver.findElement(SHARE_PRICE_LINK).getDomAttribute("aria-label");
    }
}
