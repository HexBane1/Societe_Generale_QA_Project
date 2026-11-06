# SG QA Portfolio

This is my QA portfolio project. I test Société Générale's public website (in English and French) and two public banking data endpoints, with API tests in Postman and UI tests in Selenium. Everything runs automatically on GitHub Actions.

It's a learning project. I wrote a test plan and test cases first, then built the tests from them.

## What's tested

**API tests** (Postman, run with Newman)
- **API 1–4:** the BNR (National Bank of Romania) daily exchange-rate feed. Is it up, is the XML valid, are EUR/USD/GBP there with real numbers, and is the date recent?
- **API 5:** what happens when you ask for a file that doesn't exist.
- **API 6:** Société Générale's share price endpoint (`getvalue.php`).

**UI tests** (Java, Selenium, JUnit)
- **UI 1:** the cookie banner can be declined.
- **UI 2 and UI 3:** switching the site from English to French and back.
- **UI 4:** the main menu is translated in both languages.
- **UI 5:** the English share price label has no placeholder text. **This one fails on purpose**, see below.
- **INT 1:** the share price on the website matches the price from the endpoint (within 1%).

All the details are in the [documentation](documentation/) folder:
- [Test plan](documentation/TEST_PLAN.md): what I test, why, and when I'm done.
- [Test cases](documentation/TEST_CASES.md): every test with its steps and expected result.
- [Bug reports](documentation/BUG_REPORTS.md): the bugs I found.

## Why the UI job is red

UI 5 found a real bug: on the English page, the share price link's screen reader label still says `@ price` instead of the actual price ([BUG-1](documentation/BUG_REPORTS.md)).

I kept the test failing on purpose instead of removing or disabling it. If I hid it, the pipeline would look green while the bug is still there, and nothing would tell me when it gets fixed. So the `ui-tests` job shows **6 passed, 1 failed**, and that one failure is expected.

## Project layout

```
api-tests/       Postman collection and environment
ui-tests/        Selenium tests (Maven project, Page Object Model)
documentation/   Test plan, test cases, bug reports
.github/workflows/qa.yml   The CI pipeline
```

## Running it on your computer

**API tests.** You need Node.js. Then run this from the project folder:

```
npm install -g newman newman-reporter-htmlextra
newman run api-tests/SG_QA_Portfolio_API.postman_collection.json --environment api-tests/bnr-prod.postman_environment.json
```

You can also import both files into Postman and run the collection there.

**UI tests.** You need Java 17 or newer, Maven and Chrome. Selenium downloads the right ChromeDriver by itself.

```
cd ui-tests
mvn test
```

Chrome opens in a 1920x1080 window for each test. The reports end up in `ui-tests/target/surefire-reports`.

## Running it in CI

The pipeline in `.github/workflows/qa.yml` runs:
- on every push to `main`
- every night at 02:17 (UTC)
- by hand, from the **Actions** tab, with **Run workflow**

It has two jobs:
- **api-tests:** runs the Postman collection with Newman and uploads an HTML report.
- **ui-tests:** runs the Selenium tests with Java 17 and uploads the test reports.

You can download the reports at the bottom of each run's page in the [Actions tab](https://github.com/HexBane1/Societe_Generale_QA_Project/actions).

A couple of things I ran into:
- **No headless mode:** The cookie banner doesn't show up in headless Chrome, so the UI tests break there. Instead, CI runs a normal Chrome window on a virtual screen (`xvfb-run`).
- **Bot protection:** The site uses bot protection. If it ever blocks GitHub's servers, I run the UI tests on my own computer instead and note it here.

## Findings

**Bugs** (full write-ups in [BUG_REPORTS.md](documentation/BUG_REPORTS.md))
- **BUG-1:** the English share price label for screen readers shows `@ price € with a @trend of @ diff%` instead of the real values. The French page is fine.
- **BUG-2:** on the English site, the mobile menu's back button says "Retour" (French).

**Other things I noticed**
- BNR's old feed address, `www.bnr.ro/nbrfxrates.xml`, now returns an HTML page instead of XML. My content-type check caught this, and the tests now use the current address, `curs.bnr.ro`.
- On `curs.bnr.ro`, a file that doesn't exist returns `502 Bad Gateway` instead of `404 Not Found`. No rates come back, so it's not a real problem, just something I noted.
- `getvalue.php` returns JSON, but says it's HTML (`Content-Type: text/html; charset=UTF-8`).
- The French homepage has a link to French retail-banking login ("Accès à mes comptes") that the English page doesn't have. That's for French retail customers, so I think it's on purpose.

**Small typos on the English page**
- The page description has missing spaces: "forover 150 years" and "built onthree".
- The Staff figure shows "110000" without a thousands separator (it should be "110,000").

## Rules I stuck to

These are real, live websites, so I keep it gentle:
- no logins and no forms
- no load testing or security testing
- no crawling
- only the requests each test actually needs
