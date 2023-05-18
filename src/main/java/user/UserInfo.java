package user;

public class UserInfo {
    private boolean success;
    private User user;

    public UserInfo() {
    }

    public boolean isSuccess() {
        return success;
    }

    public User getUser() {
        return user;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
