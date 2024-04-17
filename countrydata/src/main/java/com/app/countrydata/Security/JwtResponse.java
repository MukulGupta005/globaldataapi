package com.app.countrydata.Security;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class JwtResponse {

    private String jwtToken;

    private String  userName;

    private String url;

}
