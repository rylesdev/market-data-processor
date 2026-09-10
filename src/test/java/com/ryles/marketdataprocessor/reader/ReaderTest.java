package com.ryles.marketdataprocessor.reader;

import com.ryles.marketdataprocessor.exception.EmptyFileException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ReaderTest {

    private Path input;

    @BeforeEach
    void setUp() {
        this.input = Path.of("src", "test", "resources", "input");
    }

    @Test
    void readAllLinesTest() throws IOException, InterruptedException {

        Path fichier = input.resolve("test-3-lignes.txt");
        InputStream inputStream = Files.newInputStream(fichier);

        Reader reader = new Reader(inputStream);

        reader.readAllLines();

        List<String> resultats = reader.getResultats();

        assertEquals(3, resultats.size());
    }

    @Test
    void readEmptyFileTest() throws IOException {

        Path fichier = input.resolve("test-vide.txt");
        InputStream inputStream = Files.newInputStream(fichier);

        Reader reader = new Reader(inputStream);

        assertThrows(
                EmptyFileException.class,
                reader::readAllLines
        );
    }

    @Test
    void readNonExistentFileTest() {

        Path fichier = input.resolve("fichier-qui-nexiste-pas.txt");

        assertThrows(
                IOException.class,
                () -> Files.newInputStream(fichier)
        );
    }
}