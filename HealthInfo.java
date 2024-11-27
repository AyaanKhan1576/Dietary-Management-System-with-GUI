package business.models;

public class HealthInfo {
    private int id;               // ID of the health info record
    private int userId;           // ID of the user to whom this health info belongs
    private double height;        // Height of the user in cm
    private double weight;        // Weight of the user in kg
    private int age;              // Age of the user
    private String allergies;     // Allergies of the user (comma-separated, if multiple)
    private String diseases;      // Diseases of the user (comma-separated, if multiple)
    private String goal;          // User's goal for joining the app

    // Constructor
    public HealthInfo( int userId, double height, double weight, int age, String allergies, String diseases, String goal) {
        this.userId = userId;
        this.height = height;
        this.weight = weight;
        this.age = age;
        this.allergies = allergies;
        this.diseases = diseases;
        this.goal = goal;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getDiseases() {
        return diseases;
    }

    public void setDiseases(String diseases) {
        this.diseases = diseases;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    // Override toString for better debugging
    @Override
    public String toString() {
        return "HealthInfo{" +
                "id=" + id +
                ", userId=" + userId +
                ", height=" + height +
                ", weight=" + weight +
                ", age=" + age +
                ", allergies='" + allergies + '\'' +
                ", diseases='" + diseases + '\'' +
                ", goal='" + goal + '\'' +
                '}';
    }
}
