# Test Cases: SG QA Portfolio

## API tests
Preconditions for all API tests: the `bnr-prod` environment is selected in Postman, or passed to Newman with the exported environment file.

| ID | Title | Steps | Expected result | Priority | Requirement | Technique |
|---|---|---|---|---|---|---|
| API 1 | Feed availability | Send `GET {{baseUrl}}/nbrfxrates.xml` | Status is 200, response time is under 2000 ms, Content-Type contains `xml` | High | R1 | Smoke test, non-functional test (response time) |
| API 2 | Feed structure | Use the API 1 response | Body is valid XML with a `DataSet` root element, publishing date is a real date in `YYYY-MM-DD` format | High | R2 | Black-box test |
| API 3 | Currency rates | Use the API 1 response | EUR, USD and GBP are present, every rate is a number greater than 0 | High | R4 | Black-box test |
| API 4 | Feed recency | Use the API 1 response | Publishing date is not in the future and is at most 4 days old | High | R3 | Black-box test |
| API 5 | Invalid feed path | Send `GET {{baseUrl}}/nbrfxrates-does-not-exist.xml` | Response does not contain exchange-rate data | Medium | R1 | Negative test, error guessing |
| API 6 | SG share price | Send `GET {{sgBaseUrl}}/getvalue.php` | Status is 200, response time is under 2000 ms, success flag is `true`, price is a number greater than 0, price change is a number, date is not in the future and is at most 4 days old | High | R5 | Black-box test, non-functional test (response time) |

## UI tests
Preconditions for all UI tests: Chrome with a 1920x1080 window (the share price and language switch only show on desktop), a new browser session for each test, and the cookie banner declined (except in UI 1).

| ID | Title | Steps | Expected result | Priority | Requirement | Technique |
|---|---|---|---|---|---|---|
| UI 1 | Cookie banner can be declined | 1. Open `https://www.societegenerale.com/en` 2. Click "Decline all" | The cookie banner closes | High | R6 | Black-box test |
| UI 2 | Switch from English to French | 1. Open `/en` 2. Click "Fr" in the header | URL ends with `/fr`, `<html lang>` is `fr`, page title contains "Accueil" | High | R6 | Black-box test |
| UI 3 | Switch from French to English | 1. Open `/fr` 2. Click "En" in the header | URL ends with `/en`, `<html lang>` is `en`, page title contains "Home" | High | R6 | Black-box test |
| UI 4 | Main menu is translated | 1. Open `/en` and read the main menu labels 2. Repeat on `/fr` | English: The Group, Responsibility, News and documentation, Investors, Job applicants. French: Le Groupe, Responsabilité, Actualités et publications, Investisseurs, Candidats | Medium | R6 | Data-driven test (one run per language) |
| UI 5 | English share price label has no placeholder text | 1. Open `/en` 2. Wait until the share price is shown 3. Read the share price link's `aria-label` | The label contains the price shown on the page and no `@` placeholders | Medium | R6 | Error guessing, found during exploratory testing |
| INT 1 | Share price on the page matches the endpoint | 1. Open `/en` and read the share price 2. Send `GET /getvalue.php` | The two prices differ by less than 1% (the price can move between the two reads) | High | R7 | Comparing UI and API data |

**UI 5 is expected to fail.** It found a real bug during exploratory testing: on `/en`, the share price link's `aria-label` still contains the `@` placeholders (`The stock market price is: @ price € with a @trend of @ diff%`), so screen readers read them out. I keep this test failing on purpose until the bug is fixed. I didn't disable or remove it, because a green run would hide the bug and nothing would tell me when it gets fixed.
