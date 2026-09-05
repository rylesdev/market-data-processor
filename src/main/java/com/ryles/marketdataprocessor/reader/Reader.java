package com.ryles.marketdataprocessor.reader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Reader {
    private List<Path> liste;
    private Map<Path,List<String>> resultats;

    public Reader(List<Path> liste) {
        this.liste = liste;
        this.resultats = new HashMap<>();
    }

    public void readAllLines() throws IOException {
        for (Path var : liste) {
            List<String> lignes = Files.readAllLines(var);
            this.resultats.put(var,lignes);
        }
    }

    public Map<Path,List<String>> getResultats() {
        return this.resultats;
    }
}
