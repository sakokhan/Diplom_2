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
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static user.UserClient.BASE_URL;

public class UserRegistrationTest {
    private User user;
    private UserClient userClient;
    private  String token;
    private Response response;
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
    public String accessToken(){
        TokenResponse tokenResponse = response.as(TokenResponse.class);
        token = tokenResponse.getAccessToken();
        return token;
    }

    @Test
    @DisplayName("Регистрация уникального пользователя")
    @Description("Регистрация пользователя успешный сценарий")
    public void successRegistration(){
        createUser();
        userRegistration().then().statusCode(SC_OK).and().body( "success", equalTo(true), "accessToken", notNullValue(),
               "refreshToken", notNullValue(),"user.email", equalTo(user.getEmail()) , "user.name", equalTo(user.getName()));
        accessToken();

    }
    @Test
    @DisplayName("Регистрация существующего пользователя")
    @Description("Регистрация пользователя, который уже зарегистрирован")
    public void failRegistration(){
        createUser();
        userRegistration();
        accessToken();
        userRegistration().then().statusCode(SC_FORBIDDEN).body("success", equalTo(false),
                "message", equalTo("User already exists"));
    }
    @Test
    @DisplayName("Регистрация без реквизитов")
    @Description("Регистрация пользователя, когда не заполнено поле email")
    public void registrationNoEmail(){
        createUser();
        userRegistration();
        accessToken();
        user.setEmail(null);
        userRegistration().then().statusCode(SC_FORBIDDEN).body("success", equalTo(false),
                "message", equalTo("Email, password and name are required fields"));
    }
    @Test
    @DisplayName("Регистрация без реквизитов")
    @Description("Регистрация пользователя, когда не заполнено поле password")
    public void registrationNoPassword(){
        createUser();
        userRegistration();
        accessToken();
        user.setPassword(null);
        userRegistration().then().statusCode(SC_FORBIDDEN).body("success", equalTo(false),
                "message", equalTo("Email, password and name are required fields"));
    }
    @Test
    @DisplayName("Регистрация без реквизитов")
    @Description("Регистрация пользователя, когда не заполнено поле name")
    public void registrationNoName(){
        createUser();
        userRegistration();
        accessToken();
        user.setName(null);
        userRegistration().then().statusCode(SC_FORBIDDEN).body("success", equalTo(false),
                "message", equalTo("Email, password and name are required fields"));
    }
    @After
    public void tearDown(){userClient.deleteUser(user, token);}
}
