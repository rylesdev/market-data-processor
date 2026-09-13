package com.ryles.marketdataprocessor.comparator;

import com.ryles.marketdataprocessor.model.MarketData;

import java.util.Comparator;

public class MarketDataVolumeComparator implements Comparator<MarketData> {

    public int compare(MarketData mD1, MarketData mD2) {
        return Long.compare(mD1.getVolume(),mD2.getVolume());
    }
}