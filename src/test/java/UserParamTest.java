import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import user.UserRequest;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(Parameterized.class)
public class UserParamTest {

        private final String email;
        private final String password;
        private final String name;
        private static final Faker faker = new Faker();

        private final UserClient userClient;

        public UserParamTest(String email, String password, String name) {
            this.email = email;
            this.password = password;
            this.name = name;
            this.userClient = new UserClient();
        }

        @Parameterized.Parameters(name = "Creating user with parameters: email: {0}, password: {1}, name: {2}")
        public static Object[][] data() {
            return new Object[][]{
                    {null, faker.internet().password(), faker.name().firstName()},
                    {faker.internet().emailAddress(), null, faker.name().firstName()},
                    {faker.internet().emailAddress(), faker.internet().password(), null}
            };
        }

        @Test
        @DisplayName("403 FORBIDDEN: creating a user without required parameters")
        public void errorCreatingUser() {
            UserRequest userRequest = new UserRequest(email, password, name);
            Response response = userClient.userCreate(userRequest);
            GeneralResponse userError = response
                    .body()
                    .as(GeneralResponse.class);
            response.then().assertThat().statusCode(SC_FORBIDDEN);
            assertFalse(userError.isSuccess());
            assertEquals("Email, password and name are required fields", userError.getMessage());
        }
}
