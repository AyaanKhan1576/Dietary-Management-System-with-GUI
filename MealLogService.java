package business.services;

import java.util.List;

import business.models.MealLog;
import data.repositories.MealLogRepository;

public class MealLogService {
    private final MealLogRepository mealLogRepository = new MealLogRepository();

    public boolean logMeal(int userId, String mealName) {
        return mealLogRepository.logMeal(userId, mealName);
    }
    public List<MealLog> fetchUserMealLogsForToday(int userId) {
        return mealLogRepository.fetchUserMealLogsForToday(userId);
    }
    
    public int getTotalCaloriesConsumedToday(int userId) {
        return mealLogRepository.getTotalCaloriesForToday(userId);
    }
}
