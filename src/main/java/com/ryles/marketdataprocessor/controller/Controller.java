package com.ryles.marketdataprocessor.controller;

import com.ryles.marketdataprocessor.reader.Reader;
import com.ryles.marketdataprocessor.receiver.Receiver;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.WatchKey;
import java.util.List;
import java.util.Map;

public class Controller {

    public void start() throws IOException, InterruptedException {
        System.out.println("test");
        Receiver receiver = new Receiver();
        System.out.println("test1");
        WatchKey key = receiver.take();
        System.out.println("test2");
        receiver.pollEvents(key);
        System.out.println("test3");
        receiver.reset(key);
        System.out.println("test4");

        Reader reader = new Reader(receiver.getListe());
        System.out.println("test5");
        reader.readAllLines();

        for (Map.Entry<Path, List<String>> var : reader.getResultats().entrySet()) {
            System.out.println("Résultat : " + var + "\n");
        }

        System.out.println("test6");

        receiver.close();
        System.out.println("test7");
    }
}
