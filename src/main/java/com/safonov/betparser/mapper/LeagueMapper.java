package com.safonov.betparser.mapper;

import com.safonov.betparser.dto.LeagueData;
import com.safonov.schema.League;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = EventMapper.class)
public interface LeagueMapper {

    void mergeLeagues(@MappingTarget League target, League source);

    List<LeagueData> leaguesToLeagueData(List<League> league);

    @Mapping(source = "sport.name", target = "sport")
    @Mapping(source = "events", target = "matches")
    LeagueData leagueToLeagueData(League league);

}
