import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import user.UserCreds;
import user.UserRequest;
import user.UserResponse;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.*;

public class CreateUserTest {
    private final UserClient userClient = new UserClient();
    private UserRequest userRequest;
    private UserResponse userCreationResponse;

    private final Faker faker = new Faker();

    @Before
    public void setUp() {
        userRequest = new UserRequest(
                faker.internet().emailAddress(),
                faker.internet().password(),
                faker.name().firstName()
        );
    }

    @Test
    @DisplayName("200 ОК: create unique user")
    public void successfulCreatingUserUnique() {

        Response response = userClient.userCreate(userRequest);
        response.then().statusCode(SC_OK);

        userCreationResponse = response
                .body()
                .as(UserResponse.class);

        assertTrue(userCreationResponse.isSuccess());

    }

    @Test
    @DisplayName("403 FORBIDDEN: create not unique user")
    public void errorCreatingUserAlreadyExists() {

        userCreationResponse = userClient.userCreate(userRequest)
                .body()
                .as(UserResponse.class);

        Response response = userClient.userCreate(userRequest);
        response.then().statusCode(SC_FORBIDDEN);

        GeneralResponse userError = response
                .body()
                .as(GeneralResponse.class);

        assertFalse(userError.isSuccess());
        assertEquals("User already exists", userError.getMessage());
    }

    @After
    public void tearDown() {
        userClient.userDelete(UserCreds.getCredsFrom(userCreationResponse));
    }
}
