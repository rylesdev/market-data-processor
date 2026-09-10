package com.ryles.marketdataprocessor.reader;

import com.ryles.marketdataprocessor.exception.EmptyFileException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Reader {
    private InputStream fichier;
    private List<String> resultats;

    public Reader(InputStream fichier) {
        this.fichier = fichier;
        this.resultats = new ArrayList<>();
    }

    // Ajoute les lignes à la liste des résultats
    public void readAllLines() throws IOException, EmptyFileException, InterruptedException {
        Thread.sleep(500);
        List<String> lignes = new BufferedReader(new InputStreamReader(this.fichier)).lines().toList();
        if (lignes.isEmpty()) {
            throw new EmptyFileException("Le fichier est vide");
        }
        this.resultats.addAll(lignes);
    }

    // Retourne les lignes
    public List<String> getResultats() {
        return this.resultats;
    }
}