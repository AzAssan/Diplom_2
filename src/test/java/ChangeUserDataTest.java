import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.*;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class ChangeUserDataTest {

    private final UserClient userClient = new UserClient();
    private User user;
    private UserCreds userCreds;


    @Before
    public void setUp() {

        UserRequest userRequest = UserRequest.generate();
        userClient.userCreate(userRequest);

        user = new User(userRequest.getEmail(), userRequest.getName());
        userCreds = UserCreds.getCredsFrom(userClient.userLogin(userRequest).body()
                .as(UserResponse.class));
    }

    @Test
    @DisplayName("200 OK: successful change user email")
    public void testUpdateUserWithAuthorization() {

        user.setEmail(UserRequest.generate().getEmail());
        userClient.userChange(user, userCreds)
                .then()
                .assertThat()
                .statusCode(SC_OK);

        UserInfo userInfo = userClient.getUserInfo(userCreds)
                .body()
                .as(UserInfo.class);
        assertTrue(userInfo.isSuccess());
        assertEquals(user.getEmail().toLowerCase(), userInfo.getUser().getEmail());
    }

    @Test
    @DisplayName("200 OK: successful change user name")
    public void successfulChangeUserName() {
        user.setName(UserRequest.generate().getName());
        userClient.userChange(user, userCreds)
                .then()
                .assertThat()
                .statusCode(SC_OK);

        UserInfo userInfo = userClient.getUserInfo(userCreds)
                .body()
                .as(UserInfo.class);
        assertTrue(userInfo.isSuccess());
        assertEquals(user.getName(), userInfo.getUser().getName());
    }


    @Test
    @DisplayName("401 Unauthorized: should be authorised")
    public void testChangeUserDataWithoutAuthorization() {
        // try to change user data without token
        user.setEmail(UserRequest.generate().getEmail());

        Response response = userClient.userChange(user, new UserCreds("Bearer ", "test token"));
        response.then().statusCode(SC_UNAUTHORIZED);

        // verify that the response contains an error message
        String errorMessage = response.jsonPath().getString("message");
        assertFalse(response.jsonPath().getBoolean("success"));
        assertEquals("You should be authorised", errorMessage);
    }

    @After
    public void tearDown() {
        userClient.userDelete(userCreds);
    }
}


