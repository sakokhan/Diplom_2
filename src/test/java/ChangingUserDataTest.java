import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.TokenResponse;
import models.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import user.UserClient;

import static models.UserCreator.randomUserAllData;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static user.UserClient.BASE_URL;
import static utils.Utils.randomString;

public class ChangingUserDataTest {
    private User user;
    private UserClient userClient;
    private TokenResponse tokenResponse;
    private  String token;
    private  String expiredToken;
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
    public Response regResponse(){
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
        tokenResponse = authResponse.as(TokenResponse.class);
        token = tokenResponse.getAccessToken();
        return token;
    }
   @Step
    public String refreshToken(){
        tokenResponse = authResponse.as(TokenResponse.class);
        token = tokenResponse.getRefreshToken();
        return token;
    }
    @Test
    @DisplayName("Изменение данных")
    @Description("Изменение email пользователя с авторизацией")
    public void authorizedChangeEmail(){
        createUser();
        regResponse();
        authResponse();
        accessToken();
        User user1 = user.withEmail(randomString(10)+"@gmail.com");
        userClient.changeUser(user1, token).then().statusCode(SC_OK).body("success", equalTo(true));
        Assert.assertEquals(user1.getEmail(), user.getEmail());
    }
    @Test
    @DisplayName("Изменение данных")
    @Description("Изменение пароля пользователя с авторизацией")
    public void authorizedChangePassword(){
        createUser();
        regResponse();
        authResponse();
        accessToken();
        User user1 = user.withPassword(randomString(10));
        userClient.changeUser(user1, token).then().statusCode(SC_OK).body("success", equalTo(true));
        Assert.assertEquals(user1.getPassword(), user.getPassword());
    }

    @Test
    @DisplayName("Изменение данных")
    @Description("Изменение имени пользователя с авторизацией")
    public void authorizedChangeName(){
        createUser();
        regResponse();
        authResponse();
        accessToken();
        User user1 = user.withName(randomString(10));
        userClient.changeUser(user1, token).then().statusCode(SC_OK).body("success", equalTo(true));
        Assert.assertEquals(user1.getName(), user.getName());
    }

    @Test
    @DisplayName("Изменение данных")
    @Description("Изменение email неавторизованного пользователя, токен просрочен")
    public void changeEmailUnauthorized(){
        createUser();
        regResponse();
        authResponse();
        expiredToken = refreshToken();
        refreshToken();
        User user1 = user.withEmail(randomString(10)+"@gmail.com");
        userClient.logOut(expiredToken);
        userClient.changeUser(user1, expiredToken).then().statusCode(SC_UNAUTHORIZED).and().body("success",
                equalTo(false), "message", equalTo("You should be authorised"));
        Assert.assertNotSame(user1.toString(), user.getEmail().toString());
    }
    @Test
    @DisplayName("Изменение данных")
    @Description("Изменение пароля неавторизованного пользователя, токен просрочен")
    public void changePasswordUnauthorized(){
        createUser();
        regResponse();
        authResponse();
        expiredToken = refreshToken();
        refreshToken();
        User user1 = user.withPassword(randomString(10));
        userClient.logOut(expiredToken);
        userClient.changeUser(user1, expiredToken).then().statusCode(SC_UNAUTHORIZED).and().body("success",
                equalTo(false), "message", equalTo("You should be authorised"));
        Assert.assertNotSame(user1.toString(), user.getPassword().toString());
    }
    @Test
    @DisplayName("Изменение данных")
    @Description("Изменение имени неавторизованного пользователя, токен просрочен")
    public void changeNameUnauthorized(){
        createUser();
        regResponse();
        authResponse();
        expiredToken = refreshToken();
        refreshToken();
        User user1 = user.withName(randomString(10));
        userClient.logOut(expiredToken);
        userClient.changeUser(user1, expiredToken).then().statusCode(SC_UNAUTHORIZED).and().body("success",
                equalTo(false), "message", equalTo("You should be authorised"));
        Assert.assertNotSame(user1.toString(), user.getName().toString());
    }
    @After
    public void tearDown(){userClient.deleteUser(user, token);}
}
