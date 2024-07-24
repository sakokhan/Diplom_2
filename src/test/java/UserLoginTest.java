import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.TokenResponse;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.UserClient;
import static models.UserCreator.randomUserAllData;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static user.UserClient.BASE_URL;
import static utils.Utils.randomString;

public class UserLoginTest {
    private User user;
    private UserClient userClient;
    private  String token;
    private Response response;
    private Response authResponse;
    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        userClient = new UserClient();
    }
    @Step
    public User createUser(){
        user = randomUserAllData();
        return user;
    }
    @Step
    public Response userRegistration(){
        response = userClient.regUser(user);
        return response;
    }
    @Step
    public Response authResponse(){
        authResponse = userClient.authUser(user);
        return authResponse;
    }
    @Step
    public String accessToken(){
        TokenResponse tokenResponse = authResponse().as(TokenResponse.class);
        token = tokenResponse.getAccessToken();
        return token;
    }
    @Test
    @DisplayName("Успешная авторизация")
    @Description("Авторизация под существующим пользователем")
    public void successLoginUser(){
        createUser();
        userRegistration();
        authResponse().then().statusCode(SC_OK).and().body( "success", equalTo(true), "accessToken", notNullValue(),
                "refreshToken", notNullValue(),"user.email", equalTo(user.getEmail()) , "user.name", equalTo(user.getName()));
        accessToken();
    }
    @Test
    @DisplayName("Неуспешная авторизация")
    @Description("Авторизация с неверным логином")
    public void unsuccessfulAuthLogin(){
        createUser();
        userRegistration();
        authResponse();
        accessToken();
        user.setEmail(randomString(10)+"@gmail.com");
        authResponse().then().statusCode(SC_UNAUTHORIZED).and().body("success", equalTo(false),
                "message", equalTo("email or password are incorrect"));
    }
    @Test
    @DisplayName("Неуспешная авторизация")
    @Description("Авторизация с неверным паролем")
    public void unsuccessfulAuthPassword(){
        createUser();
        userRegistration();
        authResponse();
        accessToken();
        user.setPassword(randomString(12));
        authResponse().then().statusCode(SC_UNAUTHORIZED).and().body("success", equalTo(false),
                "message", equalTo("email or password are incorrect"));
    }
    @After
    public void tearDown(){userClient.deleteUser(user, token);}
}
