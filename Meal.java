package business.models;

public class Meal {
    private int id;
    private String name;
    private String type;
    private int calories;
    private String description;

    public Meal(int id, String name, String type, int calories, String description) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.calories = calories;
        this.description = description;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public int getCalories() { return calories; }
    public void setCalories(int calories) { this.calories = calories; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return name + " (" + calories + " cal)";
    }
}
