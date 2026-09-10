package com.ryles.marketdataprocessor.controller;

import com.ryles.marketdataprocessor.model.MarketData;
import com.ryles.marketdataprocessor.service.MarketDataService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/market-data")
public class MarketDataController {
    private final MarketDataService service;

    public MarketDataController(MarketDataService service) {
        this.service = service;
    }

    // Mettre un MarketData en BDD
    @PostMapping
    public MarketData insertMarketData(@RequestBody MarketData mD) {
        return this.service.insert(mD);
    }

    // Mettre un fichier dans la pipeline Controller
    @PostMapping("/files")
    public void insertFile(@RequestParam("fichier") MultipartFile fichier) throws IOException {
        Controller c = new Controller(service);
        c.process(fichier);
    }

    // Récupérer les infos d'un MarketData à partir de son ID
    @GetMapping("/{id}")
    public Optional<MarketData> selectMarketData(@PathVariable int id) {
        return this.service.select(id);
    }

    // Modifier les infos d'un MarketData à partir de l'ID d'un autre MarketData
    @PutMapping("/{id}")
    public MarketData updateMarketData(@PathVariable int id, @RequestBody MarketData mD) {
        return this.service.update(id,mD);
    }

    // Supprimer un MarketData à partir de son ID
    @DeleteMapping("/{id}")
    public void deleteMarketData(@PathVariable int id) {
        this.service.delete(id);
    }
}