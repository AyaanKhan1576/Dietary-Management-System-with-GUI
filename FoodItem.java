package business.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

public class FoodItem {
    private final StringProperty id;
    private final StringProperty name;
    private final IntegerProperty calories;

    public FoodItem(String id, String name, int calories) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.calories = new SimpleIntegerProperty(calories);
    }

    public StringProperty idProperty() {
        return id;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public IntegerProperty caloriesProperty() {
        return calories;
    }

    public String getId() {
        return id.get();
    }

    public String getName() {
        return name.get();
    }

    public int getCalories() {
        return calories.get();
    }
}
