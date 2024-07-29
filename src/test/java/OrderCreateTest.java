import io.qameta.allure.Description;
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
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static user.UserClient.BASE_URL;
import static utils.Utils.randomString;

public class OrderCreateTest{
    private TokenResponse tokenResponse;
    private  String token;
    private Response authResponse;
    private Response response;
    private OrderClient orderClient;
    private UserClient userClient;
    private User user;
    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        orderClient = new OrderClient();
        userClient = new UserClient();
    }
    @Test
    @DisplayName("Создание заказа")
    @Description("Создание заказа с авторизацией с ингредиентами")
    public void createOrderWithAuthorization(){
        user = randomUserAllData();
        response = userClient.regUser(user);
        authResponse = userClient.authUser(user);
        tokenResponse = authResponse.as(TokenResponse.class);
        token = tokenResponse.getAccessToken();
        orderClient.orders(orderClient.body(orderClient.getIngredients())).then().statusCode(SC_OK).and()
                .body("success", equalTo(true), "order.number", notNullValue());
        userClient.deleteUser(user, token);
    }
    @Test
    @DisplayName("Создание заказа")
    @Description("Создание заказа без авторизации с ингредиентами")
    public void createOrderWithoutAuthorization(){
        orderClient.orders(orderClient.body(orderClient.getIngredients())).then().statusCode(SC_OK).and()
                .body("success", equalTo(true), "order.number", notNullValue());
    }
    @Test
    @DisplayName("Создание заказа")
    @Description("Создание заказа без ингрениетов")
    public void createOrderWithoutIngredients(){
        orderClient.ordersNoIngredients().then().statusCode(SC_BAD_REQUEST).body("success", equalTo(false),
               "message", equalTo("Ingredient ids must be provided"));
    }
    @Test
    @DisplayName("Создание заказа")
    @Description("Создание заказа с неверным хешем ингредиентов")
    public void createOrderBadIngredient(){
        orderClient.orders(orderClient.body(randomString(20))).then().statusCode(SC_INTERNAL_SERVER_ERROR);
    }
}
