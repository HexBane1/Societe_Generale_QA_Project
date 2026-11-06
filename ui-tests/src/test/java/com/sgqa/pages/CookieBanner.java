package com.sgqa.pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * The cookie banner (made by Didomi) that appears on the first visit.
 */
public class CookieBanner {

    private static final By DECLINE_ALL_BUTTON = By.id("didomi-notice-disagree-button");

    private final WebDriverWait wait;

    public CookieBanner(WebDriver driver) {
        // Wait up to 15 seconds for things to happen, checking again and again until then
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /** Waits until the "Decline all" button can be clicked, then clicks it. */
    public void declineAll() {
        wait.until(ExpectedConditions.elementToBeClickable(DECLINE_ALL_BUTTON)).click();
    }

    /** Returns true if the banner disappears within 15 seconds, false if it is still showing. */
    public boolean isClosed() {
        try {
            return wait.until(ExpectedConditions.invisibilityOfElementLocated(DECLINE_ALL_BUTTON));
        } catch (TimeoutException e) {
            return false;
        }
    }
}
