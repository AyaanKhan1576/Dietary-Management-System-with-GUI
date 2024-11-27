package presentation.controllers;

import business.models.DietPlan;
import business.models.FoodItem;
import java.util.List;

public class UserController {
    public void createDietPlan(String id, String name, List<FoodItem> foodItems) {
        new DietPlan(id, name, foodItems);
    }
}
