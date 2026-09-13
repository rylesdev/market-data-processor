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

    // Ce constructeur est utilisé par la JPA/Hibernate
    // Il récupère le MD en BDD puis le transforme en MD en code avec ce constructeur
    public MarketData() {
    }

    // Ce constructeur est utilisé par le code
    // L'ID sera généré en BDD lors de l'insertion puis récupéré par Hibernate pour l'insérer
    public MarketData(String symbole, LocalDateTime date, BigDecimal prix, long volume) {
        this.symbole = symbole;
        this.date = date;
        this.prix = prix;
        this.volume = volume;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("Symbole : " + this.symbole + "\n");
        sb.append("Date : " + this.date + "\n");
        sb.append("Prix : " + this.prix + "\n");
        sb.append("Volume : " + this.volume + "\n");

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

    public void setSymbole(String symbole) {
        this.symbole = symbole;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }
}