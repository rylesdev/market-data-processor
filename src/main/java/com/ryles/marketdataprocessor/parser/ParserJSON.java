package com.ryles.marketdataprocessor.parser;

import com.ryles.marketdataprocessor.model.MarketData;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParserJSON implements Parser {
    private List<String> resultats;

    public ParserJSON(List<String> resultats) {
        this.resultats = resultats;
    }

    public List<MarketData> parsing() {
        StringBuffer sb = new StringBuffer();
        StringBuffer stack = new StringBuffer();
        List<MarketData> mD = new ArrayList<>();

        // For each qui sert à uniformiser les formats JSON pour que tout soit sur une seule ligne
        for (String foo : this.resultats) {
            sb.append(foo);
        }

        int longueur = sb.length();
        boolean flag = false;

        // Boucle qui sert à stack toutes les lignes entre { et } et qui instancie MarketData
        for (int i=0 ; i<longueur ; i++) {
            if (sb.charAt(i)=='{') {
                flag = true;
                continue;
            }

            if (flag && sb.charAt(i)!='}') {
                stack.append(sb.charAt(i));
            }

            if (flag && sb.charAt(i)=='}') {
                String[] chaine = stack.toString().split(",");

                int valDate = chaine[0].indexOf(":");
                int valSymbole = chaine[1].indexOf(":");
                int valPrix = chaine[2].indexOf(":");
                int valVolume = chaine[3].indexOf(":");

                LocalDateTime date = LocalDateTime.parse(chaine[0].substring(valDate+3,chaine[0].length()-1));
                String symbole = chaine[1].substring(valSymbole+3,chaine[1].length()-1);
                BigDecimal prix = new BigDecimal(chaine[2].substring(valPrix+2));
                long volume = Long.parseLong(chaine[3].substring(valVolume+2));

                mD.add(new MarketData(symbole,date,prix,volume));

                flag = false;
                stack.setLength(0);
            }
        }
        return mD;
    }
}
