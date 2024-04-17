package com.app.countrydata.Model;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Country {
    private Name name;
    private double area;
    private int population;
    private Map<String, String> languages;
}

