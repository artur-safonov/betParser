package com.safonov.betparser.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.safonov.betparser.client.Client;
import com.safonov.betparser.exception.FetchException;
import com.safonov.schema.Betline;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultBetsFetchService implements BetsFetchService {

    private static final String EXCEPTION_MESSAGE = "Exception occurred while fetching betlines";

    @Value("${settings.bets.url.betline}")
    private String betLinePath;

    private final Client betsClient;
    private final ObjectMapper objectMapper;

    @Override
    public List<Betline> fetchBetlines() {
        CollectionType targetClass = objectMapper.getTypeFactory().constructCollectionType(List.class, Betline.class);
        try {
            return objectMapper.readValue(betsClient.makeRequest(betLinePath), targetClass);
        } catch (IOException e) {
            throw new FetchException(EXCEPTION_MESSAGE);
        }
    }
}
