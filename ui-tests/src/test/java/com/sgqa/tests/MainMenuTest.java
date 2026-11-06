package com.sgqa.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sgqa.pages.MainMenu;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MainMenuTest extends BaseTest {

    /** The test data: one row per language, with the menu labels expected in that language. */
    static Stream<Arguments> expectedMenuLabels() {
        return Stream.of(
                Arguments.of("en", List.of("The Group", "Responsibility", "News and documentation", "Investors", "Job applicants")),
                Arguments.of("fr", List.of("Le Groupe", "Responsabilité", "Actualités et publications", "Investisseurs", "Candidats"))
        );
    }

    // JUnit runs this test once per row above. {0} puts the language in the name of each run.
    @ParameterizedTest(name = "{0}")
    @MethodSource("expectedMenuLabels")
    @DisplayName("UI 4: Main menu is translated")
    void mainMenuIsTranslated(String language, List<String> expectedLabels) {
        MainMenu mainMenu = new MainMenu(driver);

        openHomePageWithCookiesDeclined(language);

        assertEquals(expectedLabels, mainMenu.getLabels(), "Main menu labels on /" + language);
    }
}
