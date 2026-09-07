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
    private Map<String,Integer> link;

    public ParserCSV(Map<Path,List<String>> resultats) {
        this.resultats = resultats;
        this.link = new HashMap<>();
    }

    // Séparer les attributs (date, symbole...) et instancier MarketData pour les insérer puis mettre dans une list<MarketData>
    public Map<Path,List<MarketData>> parsing() {
        Map<Path,List<MarketData>> objets = new HashMap<>();
        for (Map.Entry<Path, List<String>> var : this.resultats.entrySet()) {
            List<MarketData> mD = new ArrayList<>();
            for (String foo : var.getValue()) {
                String bar = foo.trim();
                String[] chaines = bar.split(",");
                for (int i=0 ; i<4 ; i++) {
                    chaines[i] = chaines[i].trim();
                }

                if (chaines[0].equals("date") ||
                        chaines[0].equals("symbole") ||
                        chaines[0].equals("prix") ||
                        chaines[0].equals("volume")) {

                    for (int i=0 ; i<4 ; i++) {
                        this.link.put(chaines[i],(Integer)i);
                    }

                    continue;
                }

                LocalDateTime date = LocalDateTime.parse(chaines[this.link.get("date")]);
                String symbole = chaines[this.link.get("symbole")];
                BigDecimal prix = new BigDecimal(chaines[this.link.get("prix")]);
                long volume = Long.parseLong(chaines[this.link.get("volume")]);

                mD.add(new MarketData(symbole,date,prix,volume));
            }
            objets.put(var.getKey(),mD);
        }
        return objets;
    }
}