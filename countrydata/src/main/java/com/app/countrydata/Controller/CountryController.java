package com.app.countrydata.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

import com.app.countrydata.ApiResponse.ApiResponse;
import com.app.countrydata.Services.CountryService;

@RestController
@RequestMapping("/api")
public class CountryController {

    private static final Logger logger = LoggerFactory.getLogger(CountryController.class);

    @Autowired
    private CountryService countryService;
    // Endpoint mapping  for getting specific country information
    @GetMapping("/country/{name}")
    public ResponseEntity<ApiResponse> getCountryByName(@PathVariable String name) {
        logger.info("Request received for country by name: {}", name);
        ApiResponse apiResponse = countryService.getCountryByName(name);
        return ResponseEntity.ok(apiResponse);
    }

    // Endpoint  mapping for getting all countries information at once
    @GetMapping("/countries")
    public ResponseEntity<List<ApiResponse>> getAllCountries() {
        logger.info("Request received for all countries");
        List<ApiResponse> countries = countryService.getAllCountries();
        return ResponseEntity.ok(countries);
    }

    // Endpoint  mapping to search the country data based on language
    @GetMapping("/lang/{lang}")
    public List<ApiResponse> getCountryByLang(@PathVariable ("lang") String lang) {
        logger.info("Request received for countries by language: {}", lang);
        return countryService.getCountryByLang(lang);
    }

    // Endpoint mapping to search the country data based on population ,area 
    @GetMapping("/countries/search")
    public List<ApiResponse> getSortedCountries(
            @RequestParam long minPopulation,
            @RequestParam Double minArea,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int itemsPerPage,
            @RequestParam(defaultValue = "asc") String sortOrder) throws IOException {
        logger.info("Request received for sorted countries with minPopulation={}, minArea={}, pageNumber={}, itemsPerPage={}, sortOrder={}",
                minPopulation, minArea, pageNumber, itemsPerPage, sortOrder);
        return countryService.getSortedCountries(minPopulation, minArea, pageNumber, itemsPerPage, sortOrder);
    }
    // Endpoint mapping to search the country data based on population ,area  and language
    @GetMapping("/countries/searchl")
    public List<ApiResponse> getSortedlCountries(
            @RequestParam long minPopulation,
            @RequestParam Double minArea,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int itemsPerPage,
            @RequestParam(defaultValue = "asc") String sortOrder, 
            @RequestParam(defaultValue = "English") String language)throws IOException {
        logger.info("Request received for sorted countries with minPopulation={}, minArea={}, pageNumber={}, itemsPerPage={}, sortOrder={}",
                minPopulation, minArea, pageNumber, itemsPerPage, sortOrder);
        return countryService.getSortedlCountries(minPopulation, minArea, pageNumber, itemsPerPage, sortOrder,language);
    }
}
