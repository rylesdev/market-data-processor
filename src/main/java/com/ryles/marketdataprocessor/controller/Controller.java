package com.ryles.marketdataprocessor.controller;

import com.ryles.marketdataprocessor.exception.FichierIncoherentException;
import com.ryles.marketdataprocessor.producer.MarketDataProducer;
import com.ryles.marketdataprocessor.receiver.Receiver;
import com.ryles.marketdataprocessor.task.Tache;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.WatchKey;
import java.util.*;

@Component
public class Controller {
    private final MarketDataProducer producer;

    public Controller(MarketDataProducer producer) {
        this.producer = producer;
    }

    @PostConstruct
    public void init() {
        Thread thread = new Thread(() -> {
            try {
                startLocal();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        thread.start();
    }

    public void startLocal() throws IOException, InterruptedException, FichierIncoherentException {
        Receiver receiver = new Receiver(Path.of("src", "main", "resources", "input"));
        while (true) {
            WatchKey key = receiver.take();
            receiver.pollEvents(key);
            receiver.reset(key);

            for (Path fichier : receiver.getFichiers()) {
                InputStream inputStream = Files.newInputStream(fichier);
                Tache tache = new Tache(producer, inputStream, fichier.toString());
                Thread thread = new Thread(tache);
                thread.start();
            }

            receiver.resetFichier();
        }
    }

    public void process(MultipartFile fichier) throws IOException {
        InputStream inputStream = fichier.getInputStream();
        String nomFichier = fichier.getOriginalFilename();
        Tache tache = new Tache(producer,inputStream,nomFichier);
        Thread thread = new Thread(tache);
        thread.start();
    }
}