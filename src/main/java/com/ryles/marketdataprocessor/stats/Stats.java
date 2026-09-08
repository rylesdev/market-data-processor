package com.ryles.marketdataprocessor.stats;

import com.ryles.marketdataprocessor.stats.MarketDataNbElem;
import com.ryles.marketdataprocessor.comparator.MarketDataPrixComparator;
import com.ryles.marketdataprocessor.model.MarketData;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class Stats {
    private List<MarketData> liste;

    public Stats(List<MarketData> liste, String symbole) {
        this.liste = new ArrayList<>(liste);

        Iterator<MarketData> iterator = this.liste.iterator();

        while (iterator.hasNext()) {
            MarketData var = iterator.next();

            if (!var.getSymbole().equals(symbole)) {
                iterator.remove();
            }
        }
    }

    public int nbElem() {
        MarketDataNbElem m = new MarketDataNbElem();
        return m.nbElem(this.liste);
    }

    public BigDecimal prixMin() {
        if (this.liste.size()==0) {
            throw new IndexOutOfBoundsException("La liste est vide");
        }
        MarketDataPrixComparator m = new MarketDataPrixComparator();
        MarketData min = this.liste.get(0); // Obtenir le premier MarketData dans this.liste
        for (MarketData var : this.liste) {
            if (m.compare(var,min) < 0) {
                min = var;
            }
        }
        return min.getPrix();
    }

    public BigDecimal prixMax() {
        if (this.liste.size()==0) {
            throw new IndexOutOfBoundsException("La liste est vide");
        }
        MarketDataPrixComparator m = new MarketDataPrixComparator();
        MarketData max = this.liste.get(0); // Obtenir le premier MarketData dans this.liste
        for (MarketData var : this.liste) {
            if (m.compare(var,max) > 0) {
                max = var;
            }
        }
        return max.getPrix();
    }

    public BigDecimal prixMoyen() {
        if (this.liste.size()==0) {
            throw new IndexOutOfBoundsException("La liste est vide");
        }
        BigDecimal temp = BigDecimal.ZERO;

        for (MarketData var : this.liste) {
            temp = temp.add(var.getPrix());
        }

        return temp.divide(BigDecimal.valueOf(this.liste.size()), 2, RoundingMode.HALF_UP);
    }

    public long volumeTotal() {
        if (this.liste.size()==0) {
            throw new IndexOutOfBoundsException("La liste est vide");
        }
        long res = 0;
        for (MarketData var : this.liste) {
            res += var.getVolume();
        }
        return res;
    }
}