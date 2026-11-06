package com.sgqa.tests;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sgqa.api.SharePriceApi;
import com.sgqa.pages.Header;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SharePriceTest extends BaseTest {

    // Expected to FAIL while the bug is unfixed: on /en the label still contains "@ price", "@trend" and "@ diff".
    @Test
    @DisplayName("UI 5: English share price label has no placeholder text")
    void englishSharePriceLabelHasNoPlaceholderText() {
        Header header = new Header(driver);

        openHomePageWithCookiesDeclined("en");
        String price = header.getSharePrice();
        String label = header.getSharePriceLabel();

        assertNotNull(label, "The share price link should have an aria-label");
        assertAll(
                () -> assertTrue(label.contains(price), "Label should contain the price " + price + " but was: " + label),
                () -> assertFalse(label.contains("@"), "Label should not contain @ placeholders but was: " + label)
        );
    }

    @Test
    @DisplayName("INT 1: Share price on the page matches the endpoint")
    void sharePriceOnPageMatchesEndpoint() throws Exception {
        Header header = new Header(driver);

        openHomePageWithCookiesDeclined("en");
        double pagePrice = Double.parseDouble(header.getSharePrice());
        double apiPrice = SharePriceApi.getPrice();

        // The price can move between the two reads, so allow up to 1% difference
        double differencePercent = Math.abs(pagePrice - apiPrice) / apiPrice * 100;
        assertTrue(differencePercent < 1,
                "Page price " + pagePrice + " and endpoint price " + apiPrice + " differ by " + differencePercent + "%");
    }
}
