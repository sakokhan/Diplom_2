package order;

import com.google.gson.Gson;
//import io.restassured.path.json.JsonPath;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import models.OrderBody;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static io.restassured.RestAssured.given;
import static utils.Utils.rnd;

public class OrderClient {
    private Response response;
    private OrderBody orderBody;
    private final static String INGREDIENTS = "api/ingredients";
    private final static String ORDERS = "api/orders";
    private final static String ORDER_LIST = "api/orders";
    public Response ingredients(){
        return given().log().all().when().get(INGREDIENTS);
    }
    public String  getIngredients(){
        response = ingredients();
        JsonPath jsonPathValidator = response.jsonPath();
        List<String> ingredients = new ArrayList<>(jsonPathValidator.getList("data._id"));
        String ingredient = ingredients.get(rnd(0,10));
        return ingredient;
    }
    public String body(String ingredient){
        Gson gson = new Gson();
        List<String> ingredients = Collections.singletonList(ingredient);
        orderBody = new OrderBody(ingredients);
        String json = gson.toJson(orderBody);
        return json;
    }
    public Response orders(String json){
        return  given().header("Content-type", "application/json").body(json)
                .log().all().when().post(ORDERS);
    }
    public Response ordersNoIngredients(){
        return  given().header("Content-type", "application/json")
                .log().all().when().post(ORDERS);
    }
    public Response listOfCustomerOrders(String accessToken){
        return given().header("Authorization", accessToken).log().all().when().get(ORDER_LIST);
    }
    public Response listOfCustomerOrdersNoAuth(){return given().log().all().when().get(ORDER_LIST);}
}
