package project.tttn.vuhoangnam_20h1120232_hotelbookingapp.classes;

public class User {
    private String id;
    private String email;
    private String fullName;
    private String password;
    private String phone;
    private String avatarUrl = "";
    private String role; // "customer" or "hotel_owner"

    // Constructors
    public User() {
    }

    public User(String id, String email, String fullName, String password, String phone, String role) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.password = password;
        this.phone= phone;
        this.role = role;
    }

    public User(String email, String fullName, String password, String phone, String role) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.password = password;
        this.phone= phone;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
