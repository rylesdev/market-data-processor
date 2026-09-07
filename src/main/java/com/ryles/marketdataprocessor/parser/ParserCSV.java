package com.ryles.marketdataprocessor.parser;

import com.ryles.marketdataprocessor.exception.*;
import com.ryles.marketdataprocessor.model.MarketData;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParserCSV implements Parser {
    private List<String> resultats;
    private Map<String,Integer> link;
    private List<MarketData> mD;

    public ParserCSV(List<String> resultats) {
        this.resultats = resultats;
        this.link = new HashMap<>();
        this.mD = new ArrayList<>();
    }

    // Séparer les attributs (date, symbole...) et instancier MarketData pour les insérer puis mettre dans une list<MarketData>
    @Override
    public List<MarketData> parsing() throws AttributManquantException, AttributExcedantException, ChampManquantException, ValeurManquanteException {
        for (String foo : this.resultats) {
            String bar = foo.trim();

            if (bar.isEmpty()) {
                throw new ValeurManquanteException("Une ligne du fichier CSV est vide");
            }

            String[] chaines = bar.split(",",-1);

            if (chaines.length < 4) {
                throw new AttributManquantException("Il manque un attribut dans une ligne du fichier CSV");
            }

            if (chaines.length > 4) {
                throw new AttributExcedantException("Il y a un ou plusieurs attributs en trop dans une ligne du fichier CSV");
            }

            for (int i=0 ; i<4 ; i++) {
                chaines[i] = chaines[i].trim();
            }

            if (chaines[0].equals("date") ||
                    chaines[0].equals("symbole") ||
                    chaines[0].equals("prix") ||
                    chaines[0].equals("volume")) {

                for (int i=0 ; i<4 ; i++) {
                    if (    !(chaines[i].equals("date")) &&
                            !(chaines[i].equals("symbole")) &&
                            !(chaines[i].equals("prix")) &&
                            !(chaines[i].equals("volume"))     ) {
                        throw new ChampManquantException("Un champ du fichier CSV est inconnu");
                    }

                    this.link.put(chaines[i],i);
                }

                if (    (this.link.get("date")==null) ||
                        (this.link.get("symbole")==null) ||
                        (this.link.get("prix")==null) ||
                        (this.link.get("volume")==null)     ) {
                    throw new ChampManquantException("Il manque un champ dans l'en-tête du fichier CSV");
                }

                continue;
            }

            if (    (this.link.get("date")==null) ||
                    (this.link.get("symbole")==null) ||
                    (this.link.get("prix")==null) ||
                    (this.link.get("volume")==null)     ) {
                throw new ChampManquantException("L'en-tête du fichier CSV est absent ou invalide");
            }

            if (    chaines[this.link.get("date")].isEmpty() ||
                    chaines[this.link.get("symbole")].isEmpty() ||
                    chaines[this.link.get("prix")].isEmpty() ||
                    chaines[this.link.get("volume")].isEmpty()     ) {
                throw new ValeurManquanteException("Il manque une valeur dans une ligne du fichier CSV");
            }

            LocalDateTime date = LocalDateTime.parse(chaines[this.link.get("date")]);
            String symbole = chaines[this.link.get("symbole")];
            BigDecimal prix = new BigDecimal(chaines[this.link.get("prix")]);
            long volume = Long.parseLong(chaines[this.link.get("volume")]);

            this.mD.add(new MarketData(symbole,date,prix,volume));
        }

        return this.mD;
    }
}