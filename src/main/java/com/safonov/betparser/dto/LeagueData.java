package com.safonov.betparser.dto;

import lombok.Data;

import java.util.List;

@Data
public class LeagueData {

    private String sport;
    private String name;
    private List<MatchData> matches;

}
