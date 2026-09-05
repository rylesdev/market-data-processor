package com.ryles.marketdataprocessor.parser;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class Parser {
    private Map<Path, List<String>> resultats;

    public Parser(Map<Path,List<String>> resultats) {
        this.resultats = resultats;
    }

    public void verif() {
        for (Map.Entry<Path, List<String>> var : resultats.entrySet()) {
            // Faire la distinction entre les attributs de MarketData
        }
    }
}
