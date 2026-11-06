package com.sgqa.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Calls the getvalue.php endpoint, which returns the Société Générale share price as JSON
 * with the fields success, price, diff (price change) and date. The same endpoint is tested in API 6.
 */
public class SharePriceApi {

    private static final String URL = "https://www.societegenerale.com/getvalue.php";

    /** Sends one GET request and returns the "price" field as a number. */
    public static double getPrice() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(URL)).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IllegalStateException("getvalue.php returned status " + response.statusCode());
        }

        JsonNode json = new ObjectMapper().readTree(response.body());
        return json.get("price").asDouble();
    }
}
