package com.safonov.betparser.runner;

import com.safonov.betparser.dto.LeagueData;
import com.safonov.betparser.dto.MarketData;
import com.safonov.betparser.dto.MatchData;
import com.safonov.betparser.dto.RunnerData;
import com.safonov.betparser.service.processing.BetsProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static java.util.Collections.emptyList;

@Slf4j
@Component
@RequiredArgsConstructor
public class OnStartupRunner implements CommandLineRunner {

    public static final String LINE_BREAK = "\n";
    private static final String LEAGUE_PLACEHOLDER = "%s, %s\n";
    private static final String MATCH_PLACEHOLDER = "  %s, %s, %d\n";
    private static final String MARKET_PLACEHOLDER = "    %s\n";
    private static final String RUNNER_PLACEHOLDER = "      %s, %s, %d\n";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'");

    @Value("${settings.max-threads}")
    private Integer maxThreads;

    private final BetsProcessingService processingService;

    @Override
    public void run(String... args)  {
        List<LeagueData> leagues = processingService.retrieveAllLeagues(maxThreads);
        StringBuilder sb = new StringBuilder(LINE_BREAK);
        leagues.forEach(league -> addLeagueToSb(league, sb));

        log.info(sb.toString());
    }

    private void addLeagueToSb(LeagueData league, StringBuilder sb) {
        addToSb(league, sb, LEAGUE_PLACEHOLDER, LeagueData::getMatches, this::addMatchToSb, league.getSport(), league.getName());
    }

    private void addMatchToSb(MatchData match, StringBuilder sb) {
        addToSb(match, sb, MATCH_PLACEHOLDER, MatchData::getMarkets, this::addMarketToSb, match.getName(), match.getStartDate().format(FORMATTER), match.getId());
    }

    private void addMarketToSb(MarketData market, StringBuilder sb) {
        addToSb(market, sb, MARKET_PLACEHOLDER, MarketData::getRunners, this::addRunnerToSb, market.getName());
    }

    private void addRunnerToSb(RunnerData runner, StringBuilder sb) {
        addToSb(runner, sb, RUNNER_PLACEHOLDER, emptyCollectionFunction(), emptyBiConsumer(), runner.getName(), runner.getCoefficient(), runner.getId());
    }

    private <T, S> void addToSb(T toAdd, StringBuilder sb, String placeHolder,
                                Function<T, Collection<S>> nextCollectionProvider,
                                BiConsumer<S, StringBuilder> nextAdder,
                                Object... args) {
        sb.append(String.format(placeHolder, args));
        nextCollectionProvider.apply(toAdd).forEach(nextObject -> nextAdder.accept(nextObject, sb));
    }

    private <T, S> Function<T, Collection<S>> emptyCollectionFunction() {
        return t -> emptyList();
    }

    private <T, S> BiConsumer<T, S> emptyBiConsumer() {
        return (t, s) -> {};
    }
}
