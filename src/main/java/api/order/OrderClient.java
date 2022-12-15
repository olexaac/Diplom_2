package api.order;

import api.base.Client;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;


public class OrderClient extends Client {

    private final static String PATH_INGREDIENTS = "api/ingredients";
    private final static String PATH_ORDER = "api/orders";

    @Step("Запрос на получение списка ингредиентов")
    public ValidatableResponse get() {
        return given()
                .spec(getSpec())
                .when()
                .get(PATH_INGREDIENTS)
                .then()
                .log().all();
    }
    @Step("Запрос на создание заказа")
    public ValidatableResponse create(Order order, String actualAccessToken) {
        return given()
                .spec(getSpec())
                .header("authorization", actualAccessToken)
                .body(order)
                .when()
                .post(PATH_ORDER)
                .then()
                .log().all();
    }
    @Step("Запрос на получение списка заказов")
    public ValidatableResponse orders(String actualAccessToken) {
        return given()
                .spec(getSpec())
                .header("authorization", actualAccessToken)
                .when()
                .get(PATH_ORDER)
                .then()
                .log().all();
    }
}
