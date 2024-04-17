package com.app.countrydata.Services;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.app.countrydata.ApiResponse.ApiResponse;
import com.app.countrydata.Model.Country;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class CountryService{

    public static final String NO_COUNTRIES_FOUND_MESSAGE = "No countries found with a minimum population of ";
    public static final String AND_A_MINIMUM_AREA_OF = " and a minimum area of ";
    // Used logger for debugging purposes
    private static final Logger logger = LoggerFactory.getLogger(CountryService.class);
    // ObjectMapper instance for parsing JSON data
    private ObjectMapper objectMapper = new ObjectMapper();
    // Base URL for the RestCountries API
    private static final String REST_COUNTRIES_API_URL = "https://restcountries.com/v3.1/";

     /**
     * Service method to get country information by name from the API
     * @param name The name of the country to search for
     * @return ApiResponse containing the country information or an error message
     */   
    public ApiResponse getCountryByName(String name) {
        RestTemplate restTemplate = new RestTemplate();
        String url = REST_COUNTRIES_API_URL + "name/" + name + "?fullText=true";
        logger.info(url);
        try {
            // Parse the JSON response into a Map
            ObjectMapper countryobjectMapper = new ObjectMapper();
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> responseData = countryobjectMapper.readValue(restTemplate.getForObject(url, String.class), List.class);
            // Construct and return the ApiResponse object
            return new ApiResponse("All Information  "+name+" is given below", true, responseData);
        } catch (Exception e) {
            // Log or handle the exception
            logger.error("Error fetching country data: {}", e.getMessage());
            return new ApiResponse("Kindly check the country", false, e.getMessage());
        }
    }
    /**
     * Service method to get all countries from the API
     * @return List of ApiResponse containing the country information
     */ 
    public List<ApiResponse> getAllCountries() {
        RestTemplate restTemplate = new RestTemplate();
        String url = REST_COUNTRIES_API_URL + "all";

        // Assuming Country is a class that represents the structure of the JSON response
        Country[] countries = restTemplate.getForObject(url, Country[].class);

        // Convert each country's data into an ApiResponse object
        return Arrays.stream(countries)
            .map(country -> new ApiResponse("All Countries Information", true, country))
            .collect(Collectors.toList());
    }

      // fetch all countries where a given language is spoken
    /**
     * Service method to get all countries where a given language is spoken from the API
     * @param language The language to search for
     * @return List of ApiResponse containing the country information
     */  
    public List<ApiResponse> getCountryByLang(String language) {
        RestTemplate restTemplate = new RestTemplate();
        String url = REST_COUNTRIES_API_URL + "lang/" + language;

        // Specify the generic type parameters explicitly when calling getForObject()
        ParameterizedTypeReference<List<Map<String, Object>>> responseType = new ParameterizedTypeReference<>() {};
        @SuppressWarnings("null")
        ResponseEntity<List<Map<String, Object>>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
        List<Map<String, Object>> countries = responseEntity.getBody();

        // Use Optional to safely handle potential null values
        Optional<List<Map<String, Object>>> countriesOptional = Optional.ofNullable(countries);

        // Use the stream API to transform the list of countries into a list of ApiResponse objects
        return countriesOptional.stream()
            .map(country -> new ApiResponse("List of Countries where " + language + " is spoken.", true, country))
            .collect(Collectors.toList());
    }
    /**
 * Retrieves a list of countries sorted by population or area, filtered by minimum population and area,
 * and paginated according to the specified page number and items per page.
 *
 * @param minPopulation The minimum population a country must have to be included in the results.
 * @param minArea The minimum area a country must have to be included in the results.
 * @param pageNumber The page number for pagination.
 * @param itemsPerPage The number of items per page for pagination.
 * @param sortOrder The order in which to sort the countries (ascending or descending).
 * @return A list of ApiResponse objects containing information about the countries that meet the criteria.
 * @throws IOException If an error occurs during the REST API call or JSON deserialization.
 */
    public List<ApiResponse> getSortedCountries(long minPopulation, double minArea, int pageNumber, int itemsPerPage, String sortOrder) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String url=REST_COUNTRIES_API_URL + "all";
        String response = restTemplate.getForObject(url, String.class);
        logger.info(url);
        // Deserialize the JSON response into a list of Country objects
        List<Country> countries = objectMapper.readValue(response, new TypeReference<List<Country>>() {});

        // Filter countries by population and area
        List<Country> filteredCountries = countries.stream()
                .filter(country -> country.getPopulation() >= minPopulation)
                .filter(country -> country.getArea() >= minArea)
                .collect(Collectors.toList());
        logger.info("Filtered countries count: {}", filteredCountries.size());
        
        // Check if no countries were found that match the criteria
        if (filteredCountries.isEmpty()) {
            // Return a list with a single ApiResponse indicating no data was found
            String message = NO_COUNTRIES_FOUND_MESSAGE + minPopulation + AND_A_MINIMUM_AREA_OF + minArea + ".";
            return List.of(new ApiResponse(message, false, null));
        }

        // Sort the filtered countries based on the sortOrder parameter
        Comparator<Country> comparator = "asc".equalsIgnoreCase(sortOrder) ?
                Comparator.comparing(Country::getPopulation) :
                Comparator.comparing(Country::getPopulation).reversed();

        List<Country> sortedCountries = filteredCountries.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
                final String vb = filteredCountries.size() == 1? "is" : "are";

        // Calculate the start index for the page
        int startIndex = (pageNumber - 1) * itemsPerPage;
        // Limit the results to the number of items per page
        List<Country> paginatedCountries = sortedCountries.subList(startIndex, Math.min(startIndex + itemsPerPage, sortedCountries.size()));

        // Convert the list of Country objects to a list of ApiResponse objects
        return paginatedCountries.stream()
                .map(country -> new ApiResponse("There "+ vb+" "+filteredCountries.size()+ "countries with a minimum population of " + minPopulation + AND_A_MINIMUM_AREA_OF + minArea + ".", true, country))
                .collect(Collectors.toList());
    }

    /**
 * Retrieves a list of countries sorted by population or area, filtered by minimum population, area, and language,
 * and paginated according to the specified page number and items per page.
 *
 * @param minPopulation The minimum population a country must have to be included in the results.
 * @param minArea The minimum area a country must have to be included in the results.
 * @param pageNumber The page number for pagination.
 * @param itemsPerPage The number of items per page for pagination.
 * @param sortOrder The order in which to sort the countries (ascending or descending).
 * @param language The language spoken in the country. Only countries where this language is spoken will be included.
 * @return A list of ApiResponse objects containing information about the countries that meet the criteria.
 * @throws IOException If an error occurs during the REST API call or JSON deserialization.
 */
    public List<ApiResponse> getSortedlCountries(long minPopulation, double minArea, int pageNumber, int itemsPerPage, String sortOrder, String language) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String response;
        String url =REST_COUNTRIES_API_URL + "lang/"+language;
        logger.info(url);
        if (language != null) {
            // Fetch all countries first
            response = restTemplate.getForObject(url, String.class);
        } else {
            // If no language is specified, fetch all countries
            response = restTemplate.getForObject(REST_COUNTRIES_API_URL, String.class);
        }

        // Deserialize the JSON response into a list of Country objects
        List<Country> countries = objectMapper.readValue(response, new TypeReference<List<Country>>() {});

        // Filter countries by population, area, and language
        List<Country> filteredCountries = countries.stream()
                .filter(country -> country.getPopulation() >= minPopulation)
                .filter(country -> country.getArea() >= minArea)
                .collect(Collectors.toList());

        // Check if no countries were found that match the criteria
        if (filteredCountries.isEmpty()) {
            // Return a list with a single ApiResponse indicating no data was found
            return List.of(new ApiResponse(NO_COUNTRIES_FOUND_MESSAGE+ minPopulation + AND_A_MINIMUM_AREA_OF + minArea + ".", false, null));
        }

        // Sort the filtered countries based on the sortOrder parameter
        Comparator<Country> comparator = "asc".equalsIgnoreCase(sortOrder) ?
                Comparator.comparing(Country::getPopulation) :
                Comparator.comparing(Country::getPopulation).reversed();

        List<Country> sortedCountries = filteredCountries.stream()
                .sorted(comparator)
                .collect(Collectors.toList());

        // Calculate the start index for the page
        int startIndex = (pageNumber - 1) * itemsPerPage;
        // Limit the results to the number of items per page
        List<Country> paginatedCountries = sortedCountries.subList(startIndex, Math.min(startIndex + itemsPerPage, sortedCountries.size()));

        // Convert the list of Country objects to a list of ApiResponse objects
        return paginatedCountries.stream()
                .map(country -> new ApiResponse("There are " + filteredCountries.size() + " countries that meet the criteria: a minimum population of " + minPopulation + ", a minimum area of " + minArea + ", and  " + language + " is spoken."
                , true, country))
                .collect(Collectors.toList());
    }
}