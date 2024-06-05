package project.tttn.vuhoangnam_20h1120232_hotelbookingapp.authen;

public class CurrentUserManager {
    private static CurrentUserManager instance;
    private String userId;
    private String userEmail;
    private String userFullName;
    private String userRole;
    private String userPhone;
    private String avatarUrl;


    private CurrentUserManager() {
        // Khởi tạo các giá trị mặc định cho thông tin người dùng nếu cần
    }

    public static synchronized CurrentUserManager getInstance() {
        if (instance == null) {
            instance = new CurrentUserManager();
        }
        return instance;
    }

    public static void setInstance(CurrentUserManager instance) {
        CurrentUserManager.instance = instance;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
