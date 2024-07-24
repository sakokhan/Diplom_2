import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.TokenResponse;
import models.User;
import order.OrderClient;
import org.junit.Before;
import org.junit.Test;
import user.UserClient;

import static models.UserCreator.randomUserAllData;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static user.UserClient.BASE_URL;

public class ListOfOrdersTest {
    private TokenResponse tokenResponse;
    private  String token;
    private Response response;
    private Response authResponse;
    private OrderClient orderClient;
    private UserClient userClient;
    private User user;
    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        orderClient = new OrderClient();
        userClient = new UserClient();
    }
    @Step
    public String getAccessToken(){
        user = randomUserAllData();
        response = userClient.regUser(user);
        authResponse = userClient.authUser(user);
        tokenResponse = authResponse.as(TokenResponse.class);
        return token = tokenResponse.getAccessToken();
    }
    @Step
    public String getRefreshToken(){
        user = randomUserAllData();
        response = userClient.regUser(user);
        authResponse = userClient.authUser(user);
        tokenResponse = authResponse.as(TokenResponse.class);
        return token = tokenResponse.getRefreshToken();
    }
    @Step
    public void tearDown(){userClient.deleteUser(user, token);}

    @Test
    @DisplayName("Получение заказов")
    @Description("Получение заказов конкретного авторизованного пользователя")
    public void usersOrdersWithAuthorization(){
        getAccessToken();
        orderClient.orders(orderClient.body(orderClient.getIngredients()));
        orderClient.listOfCustomerOrders(token).then().statusCode(SC_OK).and().body("success", equalTo(true));
        tearDown();
    }
    @Test
    @DisplayName("Получение заказов")
    @Description("Получение заказов конкретного неавторизованного пользователя")
    public void usersOrdersNoAuthorization(){
        orderClient.listOfCustomerOrdersNoAuth().then().statusCode(SC_UNAUTHORIZED).and()
                .body("success", equalTo(false),"message", equalTo("You should be authorised"));
    }
    @Test
    @DisplayName("Получение заказов")
    @Description("Получение заказов конкретного пользователя, токен просрочен")
    public void usersOrdersExpiredToken(){
        getRefreshToken();
        orderClient.listOfCustomerOrdersNoAuth().then().statusCode(SC_UNAUTHORIZED).and()
                .body("success", equalTo(false),"message", equalTo("You should be authorised"));
        tearDown();
    }
}
