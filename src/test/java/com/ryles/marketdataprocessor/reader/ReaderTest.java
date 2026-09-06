package com.ryles.marketdataprocessor.reader;

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
    void readAllLinesTest() throws IOException {

        Path fichier = input.resolve("test-3-lignes.txt");

        Reader reader = new Reader(List.of(fichier));

        reader.readAllLines();

        Map<Path, List<String>> resultats = reader.getResultats();

        assertEquals(3, resultats.get(fichier).size());
    }

    @Test
    void readEmptyFileTest() throws IOException {

        Path fichier = input.resolve("test-vide.txt");

        Reader reader = new Reader(List.of(fichier));

        reader.readAllLines();

        assertTrue(reader.getResultats().get(fichier).isEmpty());
    }

    @Test
    void readMultipleFilesTest() throws IOException {

        Path fichier1 = input.resolve("test-3-lignes.txt");
        Path fichier2 = input.resolve("test-2-lignes.txt");

        Reader reader = new Reader(List.of(fichier1, fichier2));

        reader.readAllLines();

        assertEquals(2, reader.getResultats().size());
    }

    @Test
    void readNonExistentFileTest() {

        Path fichier = input.resolve("fichier-qui-nexiste-pas.txt");

        Reader reader = new Reader(List.of(fichier));

        assertThrows(IOException.class, reader::readAllLines);
    }
}