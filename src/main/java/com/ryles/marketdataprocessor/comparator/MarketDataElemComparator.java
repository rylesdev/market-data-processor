package com.ryles.marketdataprocessor.comparator;

import com.ryles.marketdataprocessor.model.MarketData;

import java.util.Comparator;
import java.util.List;

public class MarketDataElemComparator implements Comparator<List<MarketData>> {

    public int compare(List<MarketData> mD1, List<MarketData> mD2)  {
        return Integer.compare(mD1.size(),mD2.size());
    }
}
