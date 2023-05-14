package user;

public class UserCreds {
    private String accessToken;
    private String refreshToken;

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public UserCreds(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public UserCreds() {
    }

    public static UserCreds getCredsFrom(UserResponse userResponse) {
        return new UserCreds(userResponse.getAccessToken(), userResponse.getRefreshToken());
    }
}
