package com.safonov.betparser.service.processing;

import com.safonov.betparser.dto.LeagueData;
import com.safonov.betparser.mapper.EventMapper;
import com.safonov.betparser.mapper.LeagueMapper;
import com.safonov.betparser.service.fetch.BetsFetchService;
import com.safonov.schema.Event;
import com.safonov.schema.League;
import com.safonov.schema.Sport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

@Service
@Slf4j
@RequiredArgsConstructor
public class DefaultBetsProcessingService implements BetsProcessingService {

    @Value("${settings.bets.max-matches}")
    private Integer maxMatches;

    @Value("${settings.bets.sports}")
    private List<String> sports;

    private final BetsFetchService fetchService;
    private final LeagueMapper leagueMapper;
    private final EventMapper eventMapper;

    @Override
    public List<LeagueData> retrieveAllLeagues() {
        return leagueMapper.leaguesToLeagueData(fetchService.fetchSports().parallelStream()
                .filter(this::isAllowedSport)
                .flatMap(sport -> sport.getRegions().stream())
                .flatMap(region -> region.getLeagues().stream())
                .filter(League::getTop)
                .peek(this::fetchAndMergeLeague)
                .peek(this::limitAndPopulateEvents)
                .filter(league -> !league.getEvents().isEmpty())
                .peek(this::attachSportToLeague)
                .toList());
    }

    @Override
    public List<LeagueData> retrieveAllLeagues(int maxThreads) {
        try (ExecutorService executorService = new ForkJoinPool(maxThreads)) {
            return executorService.submit(() -> retrieveAllLeagues()).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    private void fetchAndMergeLeague(League league) {
        League fetchedLeague = fetchService.fetchLeague(league.getId());
        leagueMapper.mergeLeagues(league, fetchedLeague);
    }

    private void limitAndPopulateEvents(League league) {
        List<Event> events = league.getEvents();
        if (events.size() > maxMatches) {
            events.subList(maxMatches, events.size()).clear();
        }
        events.forEach(this::populateEvent);
    }

    private void populateEvent(Event event) {
        Event fetchedEvent = fetchService.fetchEvent(event.getId());
        eventMapper.mergeEvents(event, fetchedEvent);
    }

    private void attachSportToLeague(League league) {
        Event event = league.getEvents().get(0);
        league.setSport(event.getLeague().getSport());
    }

    private boolean isAllowedSport(Sport sport) {
        return sports.contains(sport.getFamily().toLowerCase());
    }

}
