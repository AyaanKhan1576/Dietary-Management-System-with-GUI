package business.models;

public class NewUser extends User {

    public NewUser( String name, String email, String password,String role) {
        super(name, email, password, "User");
    }

    @Override
    public String getRole() {
        return "User";  // Default role for new users is "User"
    }
}
