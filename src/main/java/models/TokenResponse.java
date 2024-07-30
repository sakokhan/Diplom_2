package models;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    public String getAccessToken() {return accessToken;}
    public String getRefreshToken() {return refreshToken;}

}
