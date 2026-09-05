package com.ryles.marketdataprocessor.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MarketData {

    private String symbole;
    private LocalDateTime date;
    private BigDecimal prix;
    private long volume;
}