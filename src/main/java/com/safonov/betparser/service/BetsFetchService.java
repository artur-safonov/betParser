package com.safonov.betparser.service;

import com.safonov.schema.Betline;

import java.util.List;

public interface BetsFetchService {

    List<Betline> fetchBetlines();

}
