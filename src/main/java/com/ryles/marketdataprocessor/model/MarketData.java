package com.ryles.marketdataprocessor.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "market_data")
public class MarketData {

    @Id
    @GeneratedValue(strategy  = GenerationType.IDENTITY)
    @Column(name = "mD_id")
    private int id;
    @Column(name = "mD_symbole")
    private String symbole;
    @Column(name = "mD_date")
    private LocalDateTime date;
    @Column(name = "mD_prix")
    private BigDecimal prix;
    @Column(name = "mD_volume")
    private long volume;

    public MarketData() {
    }

    public MarketData(String symbole, LocalDateTime date, BigDecimal prix, long volume) {
        this.symbole = symbole;
        this.date = date;
        this.prix = prix;
        this.volume = volume;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("Symbole : " + this.symbole);
        sb.append("Date : " + this.date);
        sb.append("Prix : " + this.prix);
        sb.append("Volume : " + this.volume);

        return sb.toString();
    }

    public int getId() {
        return this.id;
    }

    public String getSymbole() {
        return this.symbole;
    }

    public LocalDateTime getDate() {
        return this.date;
    }

    public BigDecimal getPrix() {
        return this.prix;
    }

    public long getVolume() {
        return this.volume;
    }
}