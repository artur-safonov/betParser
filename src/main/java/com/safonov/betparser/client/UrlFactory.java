package com.safonov.betparser.client;

import okhttp3.HttpUrl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;

@Component
public class UrlFactory {

    private static final String SCHEME = "https";

    @Value("${settings.bets.url.host}")
    private String host;

    public HttpUrl create(Map<String, String> queryParameters, String... pathSegments) {
        HttpUrl.Builder urlBuilder = new HttpUrl.Builder()
                .scheme(SCHEME)
                .host(host);

        Arrays.stream(pathSegments).forEach(urlBuilder::addPathSegment);
        queryParameters.forEach(urlBuilder::addQueryParameter);

        return urlBuilder.build();
    }

}
