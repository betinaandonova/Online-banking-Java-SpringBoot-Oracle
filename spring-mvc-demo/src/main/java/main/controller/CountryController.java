package main.controller;

import main.model.Country;
import main.service.CountryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/countries")
@CrossOrigin
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    public ResponseEntity<List<Country>> getAll() {
        return ResponseEntity.ok(countryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Country> getById(@PathVariable Long id) {
        return countryService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Country>> findByCountryName(@RequestParam String countryName) {
        return ResponseEntity.ok(countryService.findByCountryName(countryName));
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestParam String countryName) {
        countryService.insertCountry(countryName);
        return ResponseEntity.ok("Country created successfully.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id,
                                         @RequestParam String countryName) {
        countryService.updateCountry(id, countryName);
        return ResponseEntity.ok("Country updated successfully.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        countryService.deleteCountry(id);
        return ResponseEntity.ok("Country deleted successfully.");
    }
}