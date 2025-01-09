package Shared_classes;

import java.io.Serializable;

public class User implements Serializable {
	    private static final long serialVersionUID = 1L; // Add this for version control during serialization
	    private int id; // Personal ID (Primary Key)
	    private String username;
	    private String password;
	    private String firstName;
	    private String lastName;
	    private String phone;
	    private String email;
	    private UserType type;

	    public enum UserType {
	        LIBRARIAN,
	        SUBSCRIBER
	    }

	    public User(int id, String username, String password, String firstName, String lastName, String phone, String email, UserType type) {
	        this.id = id;
	        this.username = username;
	        this.password = password;
	        this.firstName = firstName;
	        this.lastName = lastName;
	        this.phone = phone;
	        this.email = email;
	        this.type = type;
	    }
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    // Validation Method
    public boolean areDetailsValid() {
        return id > 0 && username != null && !username.isEmpty() && password != null && !password.isEmpty() && type != null;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", type=" + type +
                '}';
    }
}
