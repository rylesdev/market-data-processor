package com.ryles.marketdataprocessor.receiver;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class Receiver {
    private Path path;
    private WatchService ws;
    private List<Path> liste;

    public Receiver() throws IOException {
        this.path = Path.of("src", "main", "resources", "input");
        this.ws = FileSystems.getDefault().newWatchService();
        this.path.register(this.ws, StandardWatchEventKinds.ENTRY_CREATE);
        this.liste = new ArrayList<>();
    }

    // Sert à créer une key qui va être retournée quand un fichier est créé dans input
    public WatchKey take() throws InterruptedException {
        WatchKey key = this.ws.take();
        return key;
    }

    // Va prendre la clé pour mettre dans this.liste le chemin (Path) du fichier qui a été créé dans input
    public void pollEvents(WatchKey key) {
        List<WatchEvent<?>> events = key.pollEvents();

        for (WatchEvent<?> event : events) {
            Path contexte = (Path) event.context();
            this.liste.add(this.path.resolve(contexte));
        }
    }

    public boolean reset(WatchKey key) {
        return key.reset();
    }

    public void close() throws IOException {
        this.ws.close();
    }

    public List<Path> getListe() {
        return this.liste;
    }

    public Path getPath() {
        return this.path;
    }
}