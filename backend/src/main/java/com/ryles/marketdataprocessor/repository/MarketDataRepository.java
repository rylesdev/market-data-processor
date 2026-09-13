package com.ryles.marketdataprocessor.repository;

import com.ryles.marketdataprocessor.model.MarketData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketDataRepository extends JpaRepository<MarketData, Integer> {

}