package com.ryles.marketdataprocessor.receiver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.WatchKey;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReceiverTest {

    private Path path;

    @BeforeEach
    void setUp() {
        this.path = Path.of("src", "test", "resources", "input");
    }

    @Test
    void readMultipleFilesTest() throws IOException, InterruptedException {

        Receiver receiver = new Receiver(this.path);

        Path fichier1 = this.path.resolve("test1.txt");
        Path fichier2 = this.path.resolve("test2.txt");

        Files.deleteIfExists(fichier1);
        Files.deleteIfExists(fichier2);

        Files.createFile(fichier1);

        WatchKey key1 = receiver.poll(1, TimeUnit.SECONDS);
        receiver.pollEvents(key1);
        receiver.reset(key1);

        Files.createFile(fichier2);

        WatchKey key2 = receiver.poll(1, TimeUnit.SECONDS);
        receiver.pollEvents(key2);
        receiver.reset(key2);

        assertEquals(2, receiver.getFichiers().size());

        Files.deleteIfExists(fichier1);
        Files.deleteIfExists(fichier2);

        receiver.close();
    }
}