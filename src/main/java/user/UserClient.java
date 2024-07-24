package user;

import io.restassured.response.Response;
import models.User;
import static io.restassured.RestAssured.given;

public class UserClient {
    private static final String REG_USER = "api/auth/register";
    private static final String AUTH_USER = "api/auth/login";
    public static final String BASE_URL = "https://stellarburgers.nomoreparties.site/";
    private static final String CHANGE_USER = "api/auth/user";
    private static final String LOG_OUT =  "api/auth/logout";
    public Response regUser(User user){
        return given().header("Content-type", "application/json").and().body(user).log().all().when().post(REG_USER);
    }
    public Response authUser(User user){
        return given().header("Content-type", "application/json").and().body(user).log().all().when().post(AUTH_USER);
    }
   public Response changeUser(User user, String accessToken){

        return given().header("Content-type", "application/json").header("Authorization", accessToken)
                .and().body(user).log().all().when().patch(CHANGE_USER);
    }
    public Response deleteUser(User user, String accessToken){
        return given().header("Content-type", "application/json").header("Authorization", accessToken).and().body(user).log().all().when().delete(CHANGE_USER);
    }

    public Response logOut(String refreshToken){
        return  given().header("Content-type", "application/json").body("{\"token\"" + ":" +"\""+ refreshToken+"\"}")
            .log().all().when().post(LOG_OUT);
    }
}

