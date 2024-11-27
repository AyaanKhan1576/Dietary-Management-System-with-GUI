package business.factories;

import business.models.Admin;
import business.models.RegularUser;
import business.models.User;

public class UserFactory {
    public static User createUser(String role, String id, String name, String email, String password) {
        switch (role) {
            case "Admin":
                return new Admin(id, name, email,password);
            case "User":
                return new RegularUser(id, name, email,password);
            default:
                throw new IllegalArgumentException("Invalid user role: " + role);
        }
    }
}
