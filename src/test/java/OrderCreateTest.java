import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import ingredient.IngredientList;
import order.OrderRequest;
import order.OrderResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.UserCreds;
import user.UserRequest;
import user.UserResponse;

import java.util.*;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class OrderCreateTest {
    private final IngredientClient ingredientClient = new IngredientClient();
    private IngredientList ingredientList;
    private final UserClient userClient = new UserClient();
    private final Order order = new Order();
    private UserResponse userResponse;

    @Before
    public void setUp() {
        UserRequest userRequest = UserRequest.generate();
        userClient.userCreate(userRequest);
        userResponse = userClient.userLogin(userRequest).body()
                .as(UserResponse.class);
        ingredientList = ingredientClient.getIngredientList();
    }

    @Test
    @DisplayName("200 ОК: successful order creation with token")
    public void successfulOrderCreation() {
        Response response = order.orderCreate(new OrderRequest(getRandomIngredients()),
                UserCreds.getCredsFrom(userResponse));
        response.then().assertThat().statusCode(SC_OK);
        OrderResponse orderResponse = response.body().as(OrderResponse.class);
        assertTrue(orderResponse.isSuccess());
    }

    @Test
    @DisplayName("200 ОК: successful order creation without token")
    public void successfulOrderCreationWithoutAuthorization() {
        Response response = order.orderCreateWithoutAuthorization(new OrderRequest(getRandomIngredients()));
        response.then().assertThat().statusCode(SC_OK);
        OrderResponse orderResponse = response.body().as(OrderResponse.class);
        assertTrue(orderResponse.isSuccess());
    }

    @Test
    @DisplayName("400 BAD_REQUEST: creation order without ingredients")
    public void errorOrderCreationNoIngredients() {
        Response response = order.orderCreate(new OrderRequest(Collections.emptyList()),
                UserCreds.getCredsFrom(userResponse));
        response.then().assertThat().statusCode(SC_BAD_REQUEST);
        GeneralResponse generalResponse = response.body().as(GeneralResponse.class);
        assertFalse(generalResponse.isSuccess());
        assertEquals("Ingredient ids must be provided", generalResponse.getMessage());
    }

    @Test
    @DisplayName("500 INTERNAL_SERVER_ERROR: creation order with invalid ingredient")
    public void errorOrderCreationInvalidIngredientId() {
        Response response = order.orderCreate(new OrderRequest(List.of("60d3b41abdacab0026a733")),
                UserCreds.getCredsFrom(userResponse));
        response.then().assertThat().statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @Step("get random ingredients list")
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

    @After
    public void tearDown() {
        userClient.userDelete(UserCreds.getCredsFrom(userResponse))
                .body()
                .as(UserResponse.class);
    }
}
