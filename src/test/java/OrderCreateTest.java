import api.order.Order;
import api.order.OrderClient;
import api.user.Credentials;
import api.user.UserClient;
import api.user.UserGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;


import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.empty;

public class OrderCreateTest {

    private UserClient userClient;
    private OrderClient orderClient;
    private String actualAccessToken;
    private List<String> getIngredients;
    private int getCreatingOrder;


    public OrderCreateTest() {

    }

    @Before
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Создание заказа авторизованным пользователем")
    @Description("Проверка кода ответа и номера заказа")
    public void createOrderWithAuthUser() {
        Credentials credentials = Credentials.from(UserGenerator.getDefaultUser());
        ValidatableResponse responseCreate = userClient.login(credentials);
        actualAccessToken = responseCreate.extract().path("accessToken");

        ValidatableResponse responseGetIngredient = orderClient.get();
        getIngredients = responseGetIngredient.extract().path("data._id");
        responseGetIngredient.assertThat().body("data._id", is(not(empty()))).and().statusCode(200);

        ValidatableResponse responseOrder = orderClient.create(new Order(new String[]{getIngredients.get(0),getIngredients.get(1), getIngredients.get(4)}), actualAccessToken);
        getCreatingOrder = responseOrder.extract().path("order.number");
        responseOrder.assertThat().body("order.number", is(notNullValue())).and().statusCode(200);
    }

    @Test
    @DisplayName("Создание заказа неавторизованным пользователем")
    @Description("Проверка статуса создания и номера заказа")
    public void createOrderWithoutAuthUser() {
        ValidatableResponse responseGetIngredient = orderClient.get();
        getIngredients = responseGetIngredient.extract().path("data._id");
        responseGetIngredient.assertThat().body("data._id", is(not(empty()))).and().statusCode(200);

        ValidatableResponse responseOrder = orderClient.create(new Order(new String[]{getIngredients.get(0),getIngredients.get(1), getIngredients.get(4)}), "");
        getCreatingOrder = responseOrder.extract().path("order.number");
        responseOrder.assertThat().body("order.number", is(notNullValue())).and().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Проверка статуса создания и сообщения об ошибке")
    public void createOrderWithoutIngredients() {
        ValidatableResponse responseOrder = orderClient.create(new Order(new String[]{}), "");
        responseOrder.assertThat().body("success", equalTo(false)).and().body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неправильным хешем ингредиента")
    @Description("Проверка статуса создания и сообщения об ошибке")
    public void createOrderWithIncorrectHash() {
        ValidatableResponse responseOrder = orderClient.create(new Order(new String[]{RandomStringUtils.randomAlphabetic(24)}), "");
        responseOrder.assertThat().statusCode(500);
    }
}
