package ClientApp;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {
    private final StringProperty username;
    private final StringProperty email;

    public User(String username, String email) {
        this.username = new SimpleStringProperty(username);
        this.email = new SimpleStringProperty(email);
    }

    public String getUsername() { return username.get(); }
    public String getEmail() { return email.get(); }
    public StringProperty usernameProperty() { return username; }
    public StringProperty emailProperty() { return email; }
}
