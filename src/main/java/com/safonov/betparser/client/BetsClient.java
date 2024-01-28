package com.safonov.betparser.client;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.GzipSource;
import okio.Okio;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BetsClient implements Client {

    private static final String SLASH = "/";
    private static final String QUESTION_MARK = "\\?";
    private static final String EQUAL_SIGN = "=";
    private static final String AMPERSAND = "&";
    private static final String ACCEPT_ENCODING = "Accept-Encoding";
    private static final String ENCODINGS = "gzip, deflate, br";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_TYPE_VALUE = "application/json; charset=utf-8";

    private final OkHttpClient httpClient;
    private final UrlFactory urlFactory;

    @Override
    public String makeRequest(@NonNull String path) throws IOException {
        Map<String, String> queryParameters;

        String[] urlParts = path.split(QUESTION_MARK);
        String[] pathSegments = Arrays.stream(urlParts[0].split(SLASH))
                .filter(pathSegment -> !pathSegment.isBlank())
                .toArray(String[]::new);

        if (urlParts.length > 1) {
            queryParameters = Arrays.stream(urlParts[1].split(AMPERSAND))
                    .map(queryParameter -> queryParameter.split(EQUAL_SIGN))
                    .filter(queryParameter -> queryParameter.length == 2)
                    .collect(Collectors.toMap(
                            queryParameter -> queryParameter[0],
                            queryParameter -> queryParameter[1],
                            (x, y) -> y,
                            LinkedHashMap::new));
        } else {
            queryParameters = Collections.emptyMap();
        }

        Request request = new Request.Builder()
                .url(urlFactory.create(queryParameters, pathSegments))
                .header(ACCEPT_ENCODING, ENCODINGS)
                .header(CONTENT_TYPE, CONTENT_TYPE_VALUE)
                .build();

        Response response = httpClient.newCall(request).execute();

        try (ResponseBody body = response.body()) {
            return Okio.buffer(new GzipSource(body.source())).readUtf8();
        }
    }

}
