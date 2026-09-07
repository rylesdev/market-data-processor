package com.ryles.marketdataprocessor.controller;

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

        for (Map<Path,List<String>> var : reader.getResultats()) {
            String nom = var.getKey().getFileName().toString();

            if (nom.endsWith(".csv")) {
                Parser parser = new ParserCSV(var.getValues());
                parsers.add(parser);
            } else if (nom.endsWith(".json")) {
                Parser parser = new ParserJSON(var.getValues());
                parsers.add(parser);
            }
        }

        int i = 1;
        for (Parser var : parsers) {
            System.out.println("Parsing : " + i + " : " + var.parsing() + "\n");
            ++i;
        }

        receiver.close();
    }
}
