package business.models;

import java.util.List;

public class DietPlan {
    private String id;
    private String name;
    private List<FoodItem> foodItems;

    public DietPlan(String id, String name, List<FoodItem> foodItems) {
        this.id = id;
        this.name = name;
        this.foodItems = foodItems;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<FoodItem> getFoodItems() {
        return foodItems;
    }

    public int getTotalCalories() {
        return foodItems.stream().mapToInt(FoodItem::getCalories).sum();
    }
}
