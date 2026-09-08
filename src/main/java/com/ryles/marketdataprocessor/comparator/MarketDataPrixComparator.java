package com.ryles.marketdataprocessor.comparator;

import com.ryles.marketdataprocessor.model.MarketData;

import java.math.BigDecimal;
import java.util.Comparator;

public class MarketDataPrixComparator implements Comparator<MarketData> {

    public int compare(MarketData mD1, MarketData mD2) {
        return mD1.getPrix().compareTo(mD2.getPrix());
    }
}
