package com.safonov.betparser.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.net.InetSocketAddress;
import java.net.Proxy;

@Configuration
public class ApplicationConfig {

    @Value("${settings.proxy.host}")
    private String proxyHost;

    @Value("${settings.proxy.port}")
    private Integer proxyPort;

    @Bean
    @Profile("default")
    public OkHttpClient okHttpClient() {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));

        return new OkHttpClient().newBuilder()
                .proxy(proxy)
                .build();
    }

    @Bean
    @Profile("alternative")
    public OkHttpClient okHttpClientAlternative() {
        return new OkHttpClient();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
