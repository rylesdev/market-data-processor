package com.ryles.marketdataprocessor.parser;

import com.ryles.marketdataprocessor.model.MarketData;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public interface Parser {

    List<MarketData> parsing();
}
