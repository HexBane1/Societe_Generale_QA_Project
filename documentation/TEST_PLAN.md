# Test Plan: SG QA Portfolio

> This is a personal learning project. Apart from a hackathon and a university project, I don't yet have professional testing experience, so before applying I studied the ISTQB Foundation syllabus and tried to follow its structure for a test plan as closely as I could.

## 1. Why are we testing?
To check that two public banking data sources work correctly and that the Société Générale public website works in English and French, using automated API and UI tests that run in GitHub Actions.

## 2. What are we testing?
| Item | Description |
|---|---|
| BNR exchange-rate feed | `https://curs.bnr.ro/nbrfxrates.xml`, the National Bank of Romania's public XML file with the official daily exchange rates |
| SG share price endpoint | `https://www.societegenerale.com/getvalue.php`, a JSON endpoint I found in the browser's Network tab; the SG homepage uses it to show the share price |
| SG public website | `https://www.societegenerale.com` (English and French versions) |

## 3. What should be true?
There is no official specification for the SG website or its endpoint, so I based these checks on BNR's documentation and on how the SG website currently behaves.

| ID | What should be true | Based on |
|---|---|---|
| R1 | The feed is reachable and returns XML | BNR documentation |
| R2 | The feed has the expected structure (header, publishing date, list of rates) | BNR documentation and XML schema |
| R3 | The feed shows recent rates | BNR documentation (updated after 13:00 on banking days) |
| R4 | All rates are valid positive numbers | BNR documentation |
| R5 | The share price endpoint returns a valid price, change and date | Observed behavior |
| R6 | The website works in English and French, with translated content | Observed behavior |
| R7 | The share price shown on the website matches the endpoint | Observed behavior |

## 4. What is included and excluded?
**Included**
- The two endpoints: response, structure and data
- Public pages of the SG website: cookie banner, language switch, translated titles and main menu
- The share price on the page compared with the endpoint

**Excluded**
- Search, link checking and the mobile layout (left out to keep the first version small)
- Anything behind a login
- Submitting forms or entering personal data
- Load or security testing, because these are live production sites
- Browsers other than Chrome

I keep the suite small and only add a check if its failure would tell me something important.

## 5. How will we test it, and with what tools?
| Type | Tool | Tests |
|---|---|---|
| Smoke test (is it up?) | Postman | API 1 |
| API tests | Postman, run in CI with Newman | API 2–6 |
| UI tests | Selenium 4 with Java and JUnit 5 | UI 1–5 |
| UI and API comparison | Selenium and an HTTP call | INT 1 |

I also use these test techniques:
- **Error guessing:** trying an invalid feed address, and checking the English page for placeholder text that was never replaced
- **Exploratory testing:** exploring the website to learn how it behaves before writing UI test cases
- **Data-driven testing:** running the main menu test once for English and once for French

Because these are real production sites, each test run calls each endpoint only once, scheduled runs happen at most once a night, and UI tests don't crawl the site.

## 6. What environment do we need?
- Postman and Newman (Node.js 22)
- Java 17+, Maven and Chrome. In CI, Chrome runs with a normal window on a virtual screen (`xvfb-run`), because the cookie banner does not appear in headless Chrome
- GitHub Actions on `ubuntu-latest`
- Production sites only, because no test environment is available

## 7. When can we start?
- The sites and endpoints are reachable
- Postman and the Java project are set up
- Test cases are written in `TEST_CASES.md` before the tests are added to the CI pipeline

## 8. When are we finished?
- All High-priority test cases have been run
- They pass, or each failure has been checked and documented
- The CI pipeline passes on `main`, with one exception: UI 5 fails on purpose. It found a real bug, so I keep it failing until the bug is fixed (see the note under the UI tests in `TEST_CASES.md`). The API job stays green, and the UI job is red only because of UI 5

## 9. What could go wrong?
| Risk | What I do about it |
|---|---|
| Testing on production sites | One call per endpoint per run, nightly at most, no load testing |
| The SG endpoint is undocumented and could change | If its tests fail, I check whether it changed before calling it a bug |
| No new rates on weekends and holidays | The date check allows up to 4 days |
| A website redesign could break UI tests | Page selectors are kept in one place (Page Object Model) so they're easy to update |
| Temporary network problems in CI | A failed run is re-checked before being reported |
| The feed's address can change | The old `www.bnr.ro` address now returns the homepage instead of XML; my content-type check caught this, and the tests use the current `curs.bnr.ro` address |
| The site uses bot protection, which may block a browser run from GitHub's servers | If UI tests are blocked in CI, I run them locally and note this in the README |
| I test my own work, with no second reviewer | I check each failure against the test case and the live site before reporting it |

## 10. How do we report bugs?
If I find a real bug, I document it in `BUG_REPORTS.md` with a short summary, steps to reproduce, expected and actual result, severity, and evidence.

## 11. What do we deliver?
- Postman collection and environment (`api-tests/`)
- Selenium tests (`ui-tests/`)
- GitHub Actions workflow with test reports
- `TEST_CASES.md`, and `BUG_REPORTS.md` if bugs are found (`documentation/`)
- README with a short Findings section