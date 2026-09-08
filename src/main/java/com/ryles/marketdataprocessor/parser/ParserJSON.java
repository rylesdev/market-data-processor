package com.ryles.marketdataprocessor.parser;

import com.ryles.marketdataprocessor.exception.*;
import com.ryles.marketdataprocessor.model.MarketData;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParserJSON implements Parser {
    private List<String> resultats;
    private Map<String,String> link;
    private List<MarketData> mD;

    public ParserJSON(List<String> resultats) {
        this.resultats = resultats;
        this.link = new HashMap<>();
        this.mD = new ArrayList<>();
    }

    // Sert à parser en entrant la liste des lignes d'un fichier et en retournant la liste Marketdata
    @Override
    public List<MarketData> parsing() throws AttributManquantException, AttributExcedantException, ChampManquantException, DeuxPointsManquantsException, ValeurManquanteException {
        StringBuffer sb = new StringBuffer();
        StringBuffer stack = new StringBuffer();

        // For each qui sert à uniformiser les formats JSON pour que tout soit sur une seule ligne
        for (String foo : this.resultats) {
            sb.append(foo);
        }

        int longueur = sb.length();
        boolean flag = false;

        // Boucle qui sert à stack toutes les lignes entre { et } et qui instancie MarketData
        for (int i=0 ; i<longueur ; i++) {
            if (sb.charAt(i)=='{') {
                this.link = new HashMap<>();
                flag = true;
                continue;
            }

            if (flag && sb.charAt(i)!='}') {
                stack.append(sb.charAt(i));
            }

            if (flag && sb.charAt(i)=='}') {
                String[] chaine = stack.toString().split(",");

                if (chaine.length < 4) {
                    throw new AttributManquantException("Il manque un attribut au fichier JSON");
                }

                if (chaine.length > 4) {
                    throw new AttributExcedantException("Il y a un ou plusieurs attributs en trop dans une ligne du fichier JSON");
                }

                for (int j=0 ; j<4 ; j++) {
                    String[] val = chaine[j].split(":",2);

                    if (val.length < 2) {
                        throw new DeuxPointsManquantsException("Il manque des ':' au fichier JSON");
                    }

                    val[0] = val[0].trim();
                    val[0] = val[0].substring(1,val[0].length()-1);
                    val[0] = val[0].trim();

                    val[1] = val[1].trim();

                    if (val[1].isEmpty() || val[1].equals("\"\"")) {
                        throw new ValeurManquanteException("Il manque une valeur à un attribut du fichier JSON");
                    }

                    if (val[1].charAt(0) == '"') {
                        val[1] = val[1].substring(1, val[1].length() - 1);
                        val[1] = val[1].trim();

                        if (val[1].isEmpty()) {
                            throw new ValeurManquanteException("Il manque une valeur à un attribut du fichier JSON");
                        }
                    }

                    if (    !(val[0].equals("date")) &&
                            !(val[0].equals("symbole")) &&
                            !(val[0].equals("prix")) &&
                            !(val[0].equals("volume"))     ) {
                        throw new ChampManquantException("Il manque un champ au fichier JSON");
                    }

                    this.link.put(val[0],val[1]);
                }


                if (    (this.link.get("date")==null) ||
                        (this.link.get("symbole")==null) ||
                        (this.link.get("prix")==null) ||
                        (this.link.get("volume")==null)     ) {
                    throw new AttributManquantException("Il manque un attribut au fichier JSON");
                }

                LocalDateTime date = LocalDateTime.parse(this.link.get("date"));
                String symbole = this.link.get("symbole");
                BigDecimal prix = new BigDecimal(this.link.get("prix"));
                long volume = Long.parseLong(this.link.get("volume"));

                this.mD.add(new MarketData(symbole,date,prix,volume));

                flag = false;
                stack.setLength(0);
            }
        }
        return this.mD;
    }
}
