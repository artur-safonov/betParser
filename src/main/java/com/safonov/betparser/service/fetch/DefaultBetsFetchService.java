package com.safonov.betparser.service.fetch;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.safonov.betparser.client.Client;
import com.safonov.betparser.exception.FetchException;
import com.safonov.schema.Event;
import com.safonov.schema.League;
import com.safonov.schema.Sport;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultBetsFetchService implements BetsFetchService {

    private static final String EXCEPTION_MESSAGE = "Exception occurred while fetching %s";
    private static final String SPORTS = "sports";
    private static final String LEAGUE_WITH_ID = "league with id %d";
    private static final String EVENT_WITH_ID = "event with id %d";

    @Value("${settings.bets.url.sports}")
    private String sportPath;

    @Value("${settings.bets.url.league}")
    private String leaguePath;

    @Value("${settings.bets.url.event}")
    private String eventPath;

    private final Client betsClient;
    private final ObjectMapper objectMapper;

    @Override
    public List<Sport> fetchSports() {
        CollectionType targetClass = objectMapper.getTypeFactory().constructCollectionType(List.class, Sport.class);
        return fetch(sportPath, targetClass, SPORTS);
    }

    @Override
    public League fetchLeague(Long id) {
        String path = String.format(leaguePath, id);
        String fetchObjectName = String.format(LEAGUE_WITH_ID, id);
        JavaType targetClass = objectMapper.getTypeFactory().constructType(League.class);

        return fetch(path, targetClass, fetchObjectName);
    }

    @Override
    public Event fetchEvent(Long id) {
        String path = String.format(eventPath, id);
        String fetchObjectName = String.format(EVENT_WITH_ID, id);
        JavaType targetClass = objectMapper.getTypeFactory().constructType(Event.class);

        return fetch(path, targetClass, fetchObjectName);
    }

    private <T> T fetch(String path, JavaType targetClass, String fetchObjectName) {
        try {
            return objectMapper.readValue(betsClient.makeRequest(path), targetClass);
        } catch (IOException e) {
            throw new FetchException(String.format(EXCEPTION_MESSAGE, fetchObjectName));
        }
    }
}
