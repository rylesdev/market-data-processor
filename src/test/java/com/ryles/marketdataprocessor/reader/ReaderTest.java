package com.ryles.marketdataprocessor.reader;

import com.ryles.marketdataprocessor.exception.EmptyFileException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

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

        Reader reader = new Reader(fichier);

        reader.readAllLines();

        List<String> resultats = reader.getResultats();

        assertEquals(3, resultats.size());
    }

    @Test
    void readEmptyFileTest() throws IOException, InterruptedException {

        Path fichier = input.resolve("test-vide.txt");

        Reader reader = new Reader(fichier);

        assertThrows(EmptyFileException.class, reader::readAllLines);
    }

    @Test
    void readNonExistentFileTest() {

        Path fichier = input.resolve("fichier-qui-nexiste-pas.txt");

        Reader reader = new Reader(fichier);

        assertThrows(IOException.class, reader::readAllLines);
    }
}