package com.safonov.betparser.dto;

import lombok.Data;

import java.util.List;

@Data
public class MarketData {

    private String name;
    private List<RunnerData> runners;

}
