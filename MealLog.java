package business.models;

import java.time.LocalDateTime;

public class MealLog {
    private int id;
    private int userId;
    private String mealName;
    private LocalDateTime loggedAt;

    public MealLog(int id, int userId, String mealName, LocalDateTime loggedAt) {
        this.id = id;
        this.userId = userId;
        this.mealName = mealName;
        this.loggedAt = loggedAt;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getMealName() { return mealName; }
    public void setMealName(String mealName) { this.mealName = mealName; }

    public LocalDateTime getLoggedAt() { return loggedAt; }
    public void setLoggedAt(LocalDateTime loggedAt) { this.loggedAt = loggedAt; }
}
