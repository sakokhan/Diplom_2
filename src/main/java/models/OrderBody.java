package models;

import java.util.List;
public class OrderBody {
    private List<String> ingredients;
    public List<String> getIngredients() {
        return ingredients;
    }
    public OrderBody(){}
    public OrderBody(List<String>ingredients){
        this.ingredients = ingredients;
    }
    public void setIngredients(List<String>ingredients) {
        this.ingredients = ingredients;
    }


}
