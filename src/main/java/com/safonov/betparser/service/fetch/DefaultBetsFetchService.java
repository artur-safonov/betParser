package com.safonov.betparser.service.fetch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.safonov.betparser.client.Client;
import com.safonov.betparser.exception.FetchException;
import com.safonov.schema.Sport;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultBetsFetchService implements BetsFetchService {

    private static final String EXCEPTION_MESSAGE = "Exception occurred while fetching sports";

    @Value("${settings.bets.url.sports}")
    private String sportPath;

    private final Client betsClient;
    private final ObjectMapper objectMapper;

    @Override
    public List<Sport> fetchSports() {
        CollectionType targetClass = objectMapper.getTypeFactory().constructCollectionType(List.class, Sport.class);
        try {
            return objectMapper.readValue(betsClient.makeRequest(sportPath), targetClass);
        } catch (IOException e) {
            throw new FetchException(EXCEPTION_MESSAGE);
        }
    }
}
