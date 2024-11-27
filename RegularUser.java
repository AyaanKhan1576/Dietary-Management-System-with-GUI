package business.models;

public class RegularUser extends User {
    public RegularUser(String id, String name, String email, String password) {
        super(id, name, email, password);
    }

    @Override
    public String getRole() {
        return "User";
    }
}
