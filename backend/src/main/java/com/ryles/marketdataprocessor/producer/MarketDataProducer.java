package com.ryles.marketdataprocessor.producer;

import com.ryles.marketdataprocessor.model.MarketData;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MarketDataProducer {
    private final KafkaTemplate<String, MarketData> kTemplate;

    public MarketDataProducer(KafkaTemplate<String, MarketData> kTemplate) {
        this.kTemplate = kTemplate;
    }

    public void insert(String key, MarketData mD) {
        kTemplate.send("market-data",key,mD);
    }
}