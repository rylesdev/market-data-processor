package com.ryles.marketdataprocessor.consumer;

import com.ryles.marketdataprocessor.model.MarketData;
import com.ryles.marketdataprocessor.service.MarketDataService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class MarketDataConsumer {
    private final MarketDataService service;

    public MarketDataConsumer(MarketDataService service) {
        this.service = service;
    }

    @KafkaListener(topics = "market-data", groupId = "market-data-group")
    public void receive(MarketData mD) {
        System.out.println("Résultat : " + mD);
        // this.service.insert(mD);
    }
}
