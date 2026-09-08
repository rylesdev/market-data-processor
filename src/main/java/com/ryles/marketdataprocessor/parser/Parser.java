package com.ryles.marketdataprocessor.parser;

import com.ryles.marketdataprocessor.model.MarketData;

import java.util.List;

public interface Parser {

    List<MarketData> parsing();
}