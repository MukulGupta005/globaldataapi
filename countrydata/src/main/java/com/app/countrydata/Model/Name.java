package com.app.countrydata.Model;

import java.util.Map;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class Name {
    private String common;
    private String official;
    private Map<String, NativeName> nativeName;

 
}
