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
    private List<String> resultats;
    private Map<String,Integer> link;
    List<MarketData> mD;

    public ParserCSV(List<String> resultats) {
        this.resultats = resultats;
        this.link = new HashMap<>();
        this.mD = new ArrayList<>();
    }

    // Séparer les attributs (date, symbole...) et instancier MarketData pour les insérer puis mettre dans une list<MarketData>
    @Override
    public List<MarketData> parsing() {
        for (String foo : this.resultats) {
            String bar = foo.trim();
            String[] chaines = bar.split(",");
            for (int i = 0; i < 4; i++) {
                chaines[i] = chaines[i].trim();
            }

            if (chaines[0].equals("date") ||
                    chaines[0].equals("symbole") ||
                    chaines[0].equals("prix") ||
                    chaines[0].equals("volume")) {

                for (int i = 0; i < 4; i++) {
                    this.link.put(chaines[i], i);
                }

                continue;
            }

            LocalDateTime date = LocalDateTime.parse(chaines[this.link.get("date")]);
            String symbole = chaines[this.link.get("symbole")];
            BigDecimal prix = new BigDecimal(chaines[this.link.get("prix")]);
            long volume = Long.parseLong(chaines[this.link.get("volume")]);

            this.mD.add(new MarketData(symbole, date, prix, volume));
        }
        return this.mD;
    }
}