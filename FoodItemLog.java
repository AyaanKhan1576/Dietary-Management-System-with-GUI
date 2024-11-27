package business.models;

import java.time.LocalDateTime;

public class FoodItemLog {
    private int logId;
    private int userId;
    private String foodId;
    private int totalCalories;
    private LocalDateTime logTime;

    public FoodItemLog(int logId, int userId, String foodId, int totalCalories, LocalDateTime logTime) {
        this.logId = logId;
        this.userId = userId;
        this.foodId = foodId;
        this.totalCalories = totalCalories;
        this.logTime = logTime;
    }

    // Getters and Setters
    public int getLogId() { return logId; }
    public void setLogId(int logId) { this.logId = logId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getFoodId() { return foodId; }
    public void setFoodId(String foodId) { this.foodId = foodId; }

    public int getTotalCalories() { return totalCalories; }
    public void setTotalCalories(int totalCalories) { this.totalCalories = totalCalories; }

    public LocalDateTime getLogTime() { return logTime; }
    public void setLogTime(LocalDateTime logTime) { this.logTime = logTime; }
}
