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
import java.nio.file.FileSystemException;
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

            // Lance un thread par fichier pour éviter qu'un fichier verrouillé bloque le traitement des autres
            for (Path fichier : receiver.getFichiers()) {

                Thread thread = new Thread(() -> {
                    InputStream inputStream = null;
                    boolean fichierOuvert = false;

                    while (!fichierOuvert) {
                        try {
                            inputStream = Files.newInputStream(fichier);
                            fichierOuvert = true;
                        } catch (FileSystemException e) {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException ex) {
                                Thread.currentThread().interrupt();
                                return;
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    Tache tache = new Tache(producer, inputStream, fichier.toString());
                    tache.run();
                });

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