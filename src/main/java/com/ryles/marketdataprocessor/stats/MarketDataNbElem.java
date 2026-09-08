package com.ryles.marketdataprocessor.stats;

import com.ryles.marketdataprocessor.model.MarketData;

import java.util.Comparator;
import java.util.List;

public class MarketDataNbElem {

    public int nbElem(List<MarketData> mD) {
        return mD.size();
    }
}