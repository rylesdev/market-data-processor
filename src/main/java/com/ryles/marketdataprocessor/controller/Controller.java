package com.ryles.marketdataprocessor.controller;

import com.ryles.marketdataprocessor.parser.Parser;
import com.ryles.marketdataprocessor.parser.ParserCSV;
import com.ryles.marketdataprocessor.parser.ParserJSON;
import com.ryles.marketdataprocessor.reader.Reader;
import com.ryles.marketdataprocessor.receiver.Receiver;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.WatchKey;
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

        for (Map.Entry<Path, List<String>> var : reader.getResultats().entrySet()) {
            for (String foo : var.getValue()) {
                System.out.println("Résultat : " + foo + "\n");
            }
        }

        Parser pCSV = new ParserCSV(reader.getResultats());
        Parser pJSON = new ParserJSON(reader.getResultats());

        System.out.println("ParsingCSV : " + pCSV.parsing() + "\n");
        System.out.println("ParsingJSON : " + pJSON.parsing() + "\n");

        receiver.close();
    }
}
