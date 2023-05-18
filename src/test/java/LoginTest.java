import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.UserCreds;
import user.UserRequest;
import user.UserResponse;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.*;



public class LoginTest {

        private final UserClient userClient = new UserClient();
        private UserRequest userRequest;
        private final Faker faker = new Faker();
        private UserResponse userCreationResponse;

        @Before
        public void setUp() {
            userRequest = new UserRequest(
                    faker.internet().emailAddress(),
                    faker.internet().password(),
                    faker.name().firstName()
            );
            userCreationResponse = userClient.userCreate(userRequest)
                    .body()
                    .as(UserResponse.class);
        }

        @Test
        @DisplayName("401 UNAUTHORIZED: login user with incorrect password")
        public void errorLoginUserIncorrectPassword() {
            userRequest.setPassword("incorrect_password");
            Response response = userClient.userLogin(userRequest);
            response.then().assertThat().statusCode(SC_UNAUTHORIZED);
            GeneralResponse userError = response
                    .body()
                    .as(GeneralResponse.class);
            assertFalse(userError.isSuccess());
            assertEquals("email or password are incorrect", userError.getMessage());
        }

        @Test
        @DisplayName("401 UNAUTHORIZED: login user with incorrect email")
        public void errorLoginUserIncorrectEmail() {
            userRequest.setEmail("incorrect_email");
            Response response = userClient.userLogin(userRequest);
            response.then().assertThat().statusCode(SC_UNAUTHORIZED);
            GeneralResponse userError = response
                    .body()
                    .as(GeneralResponse.class);
            assertFalse(userError.isSuccess());
            assertEquals("email or password are incorrect", userError.getMessage());
        }

        @Test
        @DisplayName("200 OK: successful login user")
        public void successfulLoginUser() {
            Response response = userClient.userLogin(userRequest);
            response.then().assertThat().statusCode(SC_OK);
            assertTrue(userClient.userLogin(userRequest)
                    .body()
                    .as(UserResponse.class)
                    .isSuccess());
        }

        @After
        public void tearDown() {
            userClient.userDelete(UserCreds.getCredsFrom(userCreationResponse));
        }
    }
