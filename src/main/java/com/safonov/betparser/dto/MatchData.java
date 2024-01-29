package com.safonov.betparser.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MatchData {

    private Long id;
    private String name;
    private LocalDateTime startDate;
    private List<MarketData> markets;

}
