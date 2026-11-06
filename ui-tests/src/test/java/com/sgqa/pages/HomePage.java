package com.sgqa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * The Société Générale homepage. Opens the English or French version.
 */
public class HomePage {

    private static final String BASE_URL = "https://www.societegenerale.com/";

    private final WebDriver driver;

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    /** Opens the homepage. language is "en" or "fr". */
    public void open(String language) {
        driver.get(BASE_URL + language);
    }

    public String getUrl() {
        return driver.getCurrentUrl();
    }

    public String getTitle() {
        return driver.getTitle();
    }

    /** Reads the lang attribute of the html tag, for example "en" from <html lang="en">. */
    public String getLanguage() {
        return driver.findElement(By.tagName("html")).getDomAttribute("lang");
    }
}
