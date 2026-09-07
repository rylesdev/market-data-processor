package com.ryles.marketdataprocessor.controller;

import com.ryles.marketdataprocessor.model.MarketData;
import com.ryles.marketdataprocessor.parser.Parser;
import com.ryles.marketdataprocessor.parser.ParserCSV;
import com.ryles.marketdataprocessor.parser.ParserJSON;
import com.ryles.marketdataprocessor.reader.Reader;
import com.ryles.marketdataprocessor.receiver.Receiver;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.WatchKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Controller {

    public void start() throws IOException, InterruptedException {
        Receiver receiver = new Receiver();
        WatchKey key = receiver.take();
        receiver.pollEvents(key);
        receiver.reset(key);

        Reader reader = new Reader(receiver.getListe());
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
            }
        }

        int i = 1;
        for (Parser var : parsers) {
            List<MarketData> marketData = var.parsing();

            System.out.println("Parsing " + i + " : \n");
            for (int j=0 ; j<marketData.size() ; j++) {
                System.out.println("MarketData num " + j + " " + marketData.get(j).toString() + "\n");
            }
            ++i;
        }

        receiver.close();
    }
}
