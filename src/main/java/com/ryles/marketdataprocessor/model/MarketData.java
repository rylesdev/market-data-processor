package com.ryles.marketdataprocessor.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class MarketData {

    private String symbole;
    private LocalDateTime date;
    private BigDecimal prix;
    private long volume;

    public MarketData(String symbole, LocalDateTime date, BigDecimal prix, long volume) {
        this.symbole = symbole;
        this.date = date;
        this.prix = prix;
        this.volume = volume;
    }
}