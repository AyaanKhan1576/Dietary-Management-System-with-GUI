package business.models;

public class DietPlanViewModel {
    private String dietPlanName;
    private String foodItemDetails;

    public DietPlanViewModel(String dietPlanName, String foodItemDetails) {
        this.dietPlanName = dietPlanName;
        this.foodItemDetails = foodItemDetails;
    }

    public String getDietPlanName() {
        return dietPlanName;
    }

    public void setDietPlanName(String dietPlanName) {
        this.dietPlanName = dietPlanName;
    }

    public String getFoodItemDetails() {
        return foodItemDetails;
    }

    public void setFoodItemDetails(String foodItemDetails) {
        this.foodItemDetails = foodItemDetails;
    }
}
