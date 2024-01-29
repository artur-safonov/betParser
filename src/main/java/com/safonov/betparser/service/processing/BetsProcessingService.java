package com.safonov.betparser.service.processing;

import com.safonov.betparser.dto.LeagueData;

import java.util.List;

public interface BetsProcessingService {

    List<LeagueData> retrieveAllLeagues();
    List<LeagueData> retrieveAllLeagues(int maxThreads);

}
