import ingredient.IngredientList;
import io.qameta.allure.Step;
import io.restassured.RestAssured;

import static io.restassured.RestAssured.given;

public class IngredientClient {

    private static final String BASE_URI = "https://stellarburgers.nomoreparties.site/";
    private static final String GET_INGREDIENT_INFO_PATH = "api/ingredients";

    public IngredientClient() {
        RestAssured.baseURI = BASE_URI;
    }

    @Step("Get ingredient list")
    public IngredientList getIngredientList() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get(GET_INGREDIENT_INFO_PATH)
                .body()
                .as(IngredientList.class);
    }
}
