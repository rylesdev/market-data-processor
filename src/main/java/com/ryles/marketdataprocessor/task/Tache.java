package com.ryles.marketdataprocessor.task;

import com.ryles.marketdataprocessor.exception.FichierIncoherentException;
import com.ryles.marketdataprocessor.model.MarketData;
import com.ryles.marketdataprocessor.parser.Parser;
import com.ryles.marketdataprocessor.parser.ParserCSV;
import com.ryles.marketdataprocessor.parser.ParserJSON;
import com.ryles.marketdataprocessor.reader.Reader;
import com.ryles.marketdataprocessor.stats.Stats;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Tache implements Runnable {
    private Path fichier;

    public Tache(Path fichier) {
        this.fichier = fichier;
    }

    @Override
    public void run() {
        Reader reader = new Reader(this.fichier);

        try {
            reader.readAllLines();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Parser parser;

        String nom = reader.getNomFichier();

        if (nom.endsWith(".csv")) {
            parser = new ParserCSV(reader.getResultats());
        } else if (nom.endsWith(".json")) {
            parser = new ParserJSON(reader.getResultats());
        } else {
            throw new FichierIncoherentException("Le type de fichier n'est pas accepté");
        }

        List<MarketData> listeMarketData = parser.parsing();

        System.out.println("Parsing : \n");
        Set<String> symboles = new HashSet<>();

        int i = 1;
        for (MarketData marketData : listeMarketData) {
            System.out.println("MarketData num " + i + " " + marketData.toString() + "\n");
            symboles.add(marketData.getSymbole());
            i++;
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
    }
}