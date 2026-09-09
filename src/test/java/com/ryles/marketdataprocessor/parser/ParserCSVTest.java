package com.ryles.marketdataprocessor.parser;

import com.ryles.marketdataprocessor.model.MarketData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserCSVTest {

    private List<String> input;

    private List<String> symbolesAttendus;
    private List<LocalDateTime> datesAttendues;
    private List<BigDecimal> prixAttendus;
    private List<Long> volumesAttendus;

    @BeforeEach
    void setUp() {

        this.input = List.of(
                "symbole,date,prix,volume",
                "BNP,2026-09-09T10:00:00,75.50,1000",
                "AIR,2026-09-09T11:00:00,180.25,2000"
        );

        this.symbolesAttendus = List.of(
                "BNP",
                "AIR"
        );

        this.datesAttendues = List.of(
                LocalDateTime.parse("2026-09-09T10:00:00"),
                LocalDateTime.parse("2026-09-09T11:00:00")
        );

        this.prixAttendus = List.of(
                new BigDecimal("75.50"),
                new BigDecimal("180.25")
        );

        this.volumesAttendus = List.of(
                1000L,
                2000L
        );
    }

    @Test
    void parsingTest() {

        ParserCSV parser = new ParserCSV(this.input);

        List<MarketData> returnParsing = parser.parsing();

        assertEquals(2, returnParsing.size());

        int i = 0;

        for (MarketData var : returnParsing) {
            assertEquals(this.symbolesAttendus.get(i), var.getSymbole());
            assertEquals(this.datesAttendues.get(i), var.getDate());
            assertEquals(this.prixAttendus.get(i), var.getPrix());
            assertEquals(this.volumesAttendus.get(i), var.getVolume());

            i++;
        }
    }
}