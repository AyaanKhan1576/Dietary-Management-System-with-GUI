package business.services;

import business.models.DietPlan;
import data.repositories.*;
import java.util.List;

public class DietPlanService {
    private final DietPlanRepository dietPlanRepository;

    public boolean updateUserDietPlan(int userId, String dietPlanId) {
        return dietPlanRepository.updateUserDietPlan(userId, dietPlanId);
    }


    public void addDietPlan(String dietPlanName) {
        dietPlanRepository.insertDietPlan(dietPlanName);
    }

    public boolean deleteDietPlan(String dietPlanId) {
        return dietPlanRepository.deleteDietPlan(dietPlanId);
    }

    public DietPlanService() {
        this.dietPlanRepository = new DietPlanRepository();
    }

    // Retrieve all diet plans with food items
    public List<DietPlan> getAllDietPlansWithFoodItems() {
        return dietPlanRepository.fetchAllDietPlansWithFoodItems();
    }
    
    public DietPlan getDietPlanForUser(int userId) {
        return dietPlanRepository.fetchDietPlanForUser(userId);
    }
    
    public List<DietPlan> searchDietPlans(String searchTerm) {
        return dietPlanRepository.searchDietPlansByName(searchTerm);
    }


    

}
