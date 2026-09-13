package com.ryles.marketdataprocessor.stats;

import com.ryles.marketdataprocessor.model.MarketData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatsTest {

    private List<MarketData> liste;
    private Stats stats;

    @BeforeEach
    void setUp() {
        this.liste = new ArrayList<>();

        this.liste.add(new MarketData(
                "BNP",
                LocalDateTime.of(2026, 9, 9, 10, 0),
                new BigDecimal("30.00"),
                100
        ));

        this.liste.add(new MarketData(
                "BNP",
                LocalDateTime.of(2026, 9, 9, 10, 1),
                new BigDecimal("40.00"),
                200
        ));

        this.liste.add(new MarketData(
                "BNP",
                LocalDateTime.of(2026, 9, 9, 10, 2),
                new BigDecimal("50.00"),
                300
        ));

        this.liste.add(new MarketData(
                "BNP",
                LocalDateTime.of(2026, 9, 9, 10, 3),
                new BigDecimal("60.00"),
                400
        ));

        this.liste.add(new MarketData(
                "BNP",
                LocalDateTime.of(2026, 9, 9, 10, 4),
                new BigDecimal("70.00"),
                500
        ));

        this.liste.add(new MarketData(
                "AIR",
                LocalDateTime.of(2026, 9, 9, 10, 5),
                new BigDecimal("1000.00"),
                10000
        ));

        this.stats = new Stats(this.liste, "BNP");
    }

    @Test
    void nbElemTest() {
        assertEquals(5, this.stats.nbElem());
    }

    @Test
    void prixMinTest() {
        assertEquals(new BigDecimal("30.00"), this.stats.prixMin());
    }

    @Test
    void prixMaxTest() {
        assertEquals(new BigDecimal("70.00"), this.stats.prixMax());
    }

    @Test
    void prixMoyenTest() {
        assertEquals(new BigDecimal("50.00"), this.stats.prixMoyen());
    }

    @Test
    void volumeTotalTest() {
        assertEquals(1500L, this.stats.volumeTotal());
    }
}