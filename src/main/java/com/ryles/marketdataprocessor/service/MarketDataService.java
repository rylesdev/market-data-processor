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

    public MarketData insert(MarketData mD) {
        return this.repository.save(mD);
    }

    public Optional<MarketData> select(MarketData mD) {
        return this.repository.findById(mD.getId());
    }

    // Ma logique c'est de mettre en input le MD actuel et le nouveau MD qu'on veut update à sa place
    // Je vais récupérer l'ID du MD actuel, puis je vais donner l'ID du MD et je vais lui dire de le remplacer par le nouveau MD
    public void update(MarketData mD, MarketData newMD) {
        Optional<MarketData> temp = this.repository.findById(mD.getId());
        int oldMD = temp.orElseThrow().getId();
        this.repository.update(oldMD,newMD);
    }

    public void delete(MarketData mD) {
        this.repository.deleteById(mD.getId());
    }
}