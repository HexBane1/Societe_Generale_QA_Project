package com.sgqa.pages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * The main menu in the header: The Group, Responsibility, News and documentation, and so on.
 */
public class MainMenu {

    // Every top-level menu item, skipping the separator items between them
    private static final By MENU_ITEMS = By.cssSelector("#nav-menu-v2 ul[data-region='header'] > li:not(.separator)");
    // Items with a sub-menu show their label in a button; plain items show it in a link
    private static final By BUTTON_LABEL = By.cssSelector("button span");
    private static final By LINK_LABEL = By.tagName("a");

    private final WebDriverWait wait;

    public MainMenu(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /** Returns the label of each main menu item, in the order they appear. */
    public List<String> getLabels() {
        List<WebElement> items = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(MENU_ITEMS));

        List<String> labels = new ArrayList<>();
        for (WebElement item : items) {
            List<WebElement> buttonLabels = item.findElements(BUTTON_LABEL);
            if (!buttonLabels.isEmpty()) {
                labels.add(buttonLabels.get(0).getText().trim());
            } else {
                labels.add(item.findElement(LINK_LABEL).getText().trim());
            }
        }
        return labels;
    }
}
