package com.ryles.marketdataprocessor.controller;

import com.ryles.marketdataprocessor.exception.FichierIncoherentException;
import com.ryles.marketdataprocessor.model.MarketData;
import com.ryles.marketdataprocessor.parser.Parser;
import com.ryles.marketdataprocessor.parser.ParserCSV;
import com.ryles.marketdataprocessor.parser.ParserJSON;
import com.ryles.marketdataprocessor.reader.Reader;
import com.ryles.marketdataprocessor.receiver.Receiver;
import com.ryles.marketdataprocessor.stats.Stats;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.WatchKey;
import java.util.*;

public class Controller {

    public void start() throws IOException, InterruptedException, FichierIncoherentException {
        Receiver receiver = new Receiver();
        while (true) {
            WatchKey key = receiver.take();
            receiver.pollEvents(key);
            receiver.reset(key);

            Reader reader = new Reader(receiver.getListe());
            receiver.resetListe();
            reader.readAllLines();

            List<Parser> parsers = new ArrayList<>();

            for (Map.Entry<Path, List<String>> var : reader.getResultats().entrySet()) {
                String nom = var.getKey().getFileName().toString();

                if (nom.endsWith(".csv")) {
                    Parser parser = new ParserCSV(var.getValue());
                    parsers.add(parser);
                } else if (nom.endsWith(".json")) {
                    Parser parser = new ParserJSON(var.getValue());
                    parsers.add(parser);
                } else {
                    throw new FichierIncoherentException("Le type de fichier n'est pas accepté");
                }
            }

            int i = 1;
            for (Parser var : parsers) {
                List<MarketData> listeMarketData = var.parsing();

                System.out.println("Parsing " + i + " : \n");

                Set<String> symboles = new HashSet<>();

                int j = 1;
                for (MarketData marketData : listeMarketData) {
                    System.out.println("MarketData num " + j + " " + marketData.toString() + "\n");
                    symboles.add(marketData.getSymbole());
                    j++;
                }

                for (String symbole : symboles) {
                    Stats stats = new Stats(listeMarketData, symbole);

                    System.out.println("Statistiques " + symbole + " :\n");
                    System.out.println("Nombre : " + stats.nbElem() + "\n");
                    System.out.println("Prix min : " + stats.prixMin() + "\n");
                    System.out.println("Prix max : " + stats.prixMax() + "\n");
                    System.out.println("Prix moyen : " + stats.prixMoyen() + "\n");
                    System.out.println("Volume total : " + stats.volumeTotal() + "\n");
                }

                ++i;
            }
        }
    }
}
