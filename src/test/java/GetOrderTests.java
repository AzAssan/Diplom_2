import com.github.javafaker.Faker;
import ingredient.IngredientList;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import order.OrderRequest;
import order.OrderResponse;
import order.OrderUser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.UserCreds;
import user.UserRequest;
import user.UserResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.*;

public class GetOrderTests {
    private final IngredientClient ingredientClient = new IngredientClient();
    private IngredientList ingredientList;
    private final UserClient userClient = new UserClient();
    private final Order order = new Order();
    private UserResponse userResponse;
    private final Faker faker = new Faker();

    @Before
    public void setUp() {
        UserRequest userRequest = new UserRequest(
                faker.internet().emailAddress(),
                faker.internet().password(),
                faker.name().firstName()
        );

        userClient.userCreate(userRequest);
        userResponse = userClient.userLogin(userRequest).body()
                .as(UserResponse.class);
        ingredientList = ingredientClient.getIngredientList();
    }

    @Test
    @DisplayName("200 OK: successful receipt of user orders")
    public void successfulGetOrdersUser() {
        List<String> orderIngredients_1 = getRandomIngredients();
        List<String> orderIngredients_2 = getRandomIngredients();
        OrderResponse order_1 = getOrderResponse(orderIngredients_1);
        OrderResponse order_2 = getOrderResponse(orderIngredients_2);
        Response response = order.getOrdersUser(UserCreds.getCredsFrom(userResponse));
        response.then().assertThat().statusCode(SC_OK);
        OrderUser ordersUser = response.body().as(OrderUser.class);
        assertTrue(ordersUser.isSuccess());
        assertEquals(ordersUser.getOrders().get(0).getIngredients(), orderIngredients_1);
        assertEquals(ordersUser.getOrders().get(1).getIngredients(), orderIngredients_2);
        assertEquals(order_1.getOrder().getNumber(), ordersUser.getOrders().get(0).getNumber());
        assertEquals(order_2.getOrder().getNumber(), ordersUser.getOrders().get(1).getNumber());
    }

    @Test
    @DisplayName("401 UNAUTHORIZED: receipt of user orders without token")
    public void errorGetOrdersUserUnauthorized() {
        Response response = order.getOrdersUserWithoutAuthorization();
        response.then().assertThat().statusCode(SC_UNAUTHORIZED);
        GeneralResponse generalResponse = response.body().as(GeneralResponse.class);
        assertFalse(generalResponse.isSuccess());
        assertEquals("You should be authorised", generalResponse.getMessage());
    }

    private List<String> getRandomIngredients() {
        List<String> ingredients = new ArrayList<>();
        Random random = new Random();
        int count = random.nextInt(ingredientList.getData().size());
        for (int i = 0; i < count; i++) {
            int index = random.nextInt(ingredientList.getData().size());
            ingredients.add(ingredientList.getData().get(index).get_id());
        }
        return ingredients;
    }

    private OrderResponse getOrderResponse(List<String> orderIngredients) {
        return order
                .orderCreate(new OrderRequest(orderIngredients), UserCreds.getCredsFrom(userResponse))
                .body()
                .as(OrderResponse.class);
    }

    @After
    public void tearDown() {
        userClient.userDelete(UserCreds.getCredsFrom(userResponse))
                .body()
                .as(UserResponse.class);
    }
}
