package com.safonov.betparser.mapper;

import com.safonov.betparser.dto.MatchData;
import com.safonov.schema.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = RunnerMapper.class)
public interface EventMapper {

    String ZONE_ID = "UTC";

    void mergeEvents(@MappingTarget Event target, Event source);

    List<MatchData> eventsToMatches(List<Event> events);

    @Mapping(source = "kickoff", target = "startDate", qualifiedByName = "kickoffToStartDate")
    MatchData eventToMatch(Event event);

    @Named("kickoffToStartDate")
    default LocalDateTime kickoffToStartDate(Long kickoff) {
        return Instant.ofEpochMilli(kickoff).atZone(ZoneId.of(ZONE_ID)).toLocalDateTime();
    }

}
