package com.ryles.marketdataprocessor.parser;

import com.ryles.marketdataprocessor.model.MarketData;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParserCSV implements Parser {
    private Map<Path, List<String>> resultats;

    public ParserCSV(Map<Path,List<String>> resultats) {
        this.resultats = resultats;
    }

    // Séparer les attributs (date, symbole...) et instancier MarketData pour les insérer puis mettre dans une list<MarketData>
    public Map<Path,List<MarketData>> parsing() {
        Map<Path,List<MarketData>> objets = new HashMap<>();
        for (Map.Entry<Path, List<String>> var : this.resultats.entrySet()) {
            List<MarketData> mD = new ArrayList<>();
            for (String foo : var.getValue()) {
                if (foo.equals("date,symbole,prix,volume")) {
                    continue;
                }

                String[] chaines = foo.split(",");

                LocalDateTime date = LocalDateTime.parse(chaines[0]);
                String symbole = chaines[1];
                BigDecimal prix = new BigDecimal(chaines[2]);
                long volume = Long.parseLong(chaines[3]);

                mD.add(new MarketData(symbole,date,prix,volume));
            }
            objets.put(var.getKey(),mD);
        }
        return objets;
    }
}
