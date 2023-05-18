import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import user.User;
import user.UserCreds;
import user.UserRequest;

import static io.restassured.RestAssured.given;

public class UserClient {
        private static final String BASE_URI = "https://stellarburgers.nomoreparties.site/";
        private static final String CREATE_USER_PATH = "api/auth/register";
        private static final String LOGIN_USER_PATH = "api/auth/login";
        private static final String USER_PATH = "api/auth/user";

        public UserClient() {
            RestAssured.baseURI = BASE_URI;
        }

        @Step("Create user")
        public Response userCreate(UserRequest user) {
            return given()
                    .header("Content-type", "application/json")
                    .body(user)
                    .post(CREATE_USER_PATH);
        }

        @Step("Login user")
        public Response userLogin(UserRequest user) {
            return given()
                    .header("Content-type", "application/json")
                    .body(user)
                    .when()
                    .post(LOGIN_USER_PATH);
        }

        @Step("Delete user")
        public Response userDelete(UserCreds userCreds) {
            return given()
                    .header("Authorization", userCreds.getAccessToken())
                    .when()
                    .delete(USER_PATH);
        }

        @Step("Change user")
        public Response userChange(User user, UserCreds userCreds) {
            return given()
                    .header("Content-type", "application/json")
                    .header("Authorization", userCreds.getAccessToken())
                    .body(user)
                    .when()
                    .patch(USER_PATH);
        }

        @Step("Get user info")
        public Response getUserInfo(UserCreds userCreds) {
            return given()
                    .header("Content-type", "application/json")
                    .header("Authorization", userCreds.getAccessToken())
                    .when()
                    .get(USER_PATH);
        }
    }
