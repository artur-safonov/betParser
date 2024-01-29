package com.safonov.betparser.service.fetch;

import com.safonov.schema.Event;
import com.safonov.schema.League;
import com.safonov.schema.Sport;

import java.util.List;

public interface BetsFetchService {

    List<Sport> fetchSports();

    League fetchLeague(Long id);

    Event fetchEvent(Long id);

}
