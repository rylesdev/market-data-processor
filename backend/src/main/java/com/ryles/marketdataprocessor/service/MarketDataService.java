package com.ryles.marketdataprocessor.service;

import com.ryles.marketdataprocessor.model.MarketData;
import org.springframework.stereotype.Service;
import com.ryles.marketdataprocessor.repository.MarketDataRepository;

import java.util.Optional;

@Service
public class MarketDataService {
    private final MarketDataRepository repository;

    public MarketDataService(MarketDataRepository repository) {
        this.repository = repository;
    }

    // Create
    public MarketData insert(MarketData mD) {
        return this.repository.save(mD);
    }

    // Read
    public Optional<MarketData> select(int id) {
        return this.repository.findById(id);
    }

    // Update
    public MarketData update(int id, MarketData newMD) {
        MarketData mD = this.repository.findById(id).orElseThrow();

        mD.setSymbole(newMD.getSymbole());
        mD.setDate(newMD.getDate());
        mD.setPrix(newMD.getPrix());
        mD.setVolume(newMD.getVolume());

        return this.repository.save(mD);
    }

    // Delete
    public void delete(int id) {
        this.repository.deleteById(id);
    }
}