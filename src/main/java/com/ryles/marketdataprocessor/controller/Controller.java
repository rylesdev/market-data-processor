package com.ryles.marketdataprocessor.controller;

import com.ryles.marketdataprocessor.exception.FichierIncoherentException;
import com.ryles.marketdataprocessor.receiver.Receiver;
import com.ryles.marketdataprocessor.task.Tache;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.WatchKey;
import java.util.*;

public class Controller {

    public void start() throws IOException, InterruptedException, FichierIncoherentException {
        Receiver receiver = new Receiver(Path.of("src", "main", "resources", "input"));
        while (true) {
            WatchKey key = receiver.take();
            receiver.pollEvents(key);
            receiver.reset(key);

            for (Path fichier : receiver.getFichiers()) {
                Tache tache = new Tache(fichier);
                Thread thread = new Thread(tache);
                thread.start();
            }

            receiver.resetFichier();
        }
    }
}