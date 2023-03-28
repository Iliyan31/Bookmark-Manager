package bg.sofia.uni.fmi.mjt.bookmarks.manager;

import bg.sofia.uni.fmi.mjt.bookmarks.entities.bitly.BitlyResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class BitlyCommunicator {
    /**
     * This is optional in the constructor
     */
    private static final String GROUP_GUID = "Bn277pCF89T";
    private static final String DOMAIN = "bit.ly";
    private static final String API_ENDPOINT_SCHEME = "https";
    private static final String HOST = "api-ssl.bitly.com";
    private static final String PATH = "/v4/shorten";
    private static final int OK_STATUS_CODE = 200;
    private static final int OK_CREATED = 201;
    private final HttpClient httpClient;
    private final String apiKey;
    private final Gson gson;

    public BitlyCommunicator(HttpClient httpClient, String apiKey) {
        this.httpClient = httpClient;
        this.apiKey = apiKey;
        this.gson = new Gson();
    }

    public String shortenBookmarkURL(String bookmarkURL)
        throws URISyntaxException, InterruptedException, IOException {
        URI uri = new URI(API_ENDPOINT_SCHEME, HOST, PATH, null);

        String postBody =
            String.format("{\"group_guid\": \"%s\", \"domain\": \"%s\", \"long_url\": \"%s\"}", GROUP_GUID, DOMAIN,
                bookmarkURL);

//        String postBodyWithoutGroupId =
//            String.format("{\"domain\": \"%s\", \"long_url\": \"%s\"}", DOMAIN,
//                bookmarkURL);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(uri)
            .setHeader("Authorization", apiKey)
            .setHeader("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(postBody))
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        validateStatusCode(response);

        BitlyResponse bitlyResponse = gson.fromJson(response.body(), BitlyResponse.class);
        return bitlyResponse.link();
    }

    private void validateStatusCode(HttpResponse<String> response) {
        if (response.statusCode() != OK_STATUS_CODE && response.statusCode() != OK_CREATED) {
            throw new RuntimeException("The external system was unable to answer your request! Please try again!");
        }
    }
}
