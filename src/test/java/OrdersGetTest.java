import api.order.OrderClient;
import api.user.Credentials;
import api.user.UserClient;
import api.user.UserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertEquals;

public class OrdersGetTest {

    private UserClient userClient;
    private OrderClient orderClient;
    private String actualAccessToken;
    private List<String> getOrders;


    public OrdersGetTest() {

    }

    @Before
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Получение списка заказов пользователя")
    @Description("Проверка получения существующих заказов и кода ответа")
    public void getOrdersWithAuthUser() {
        Credentials credentials = Credentials.from(UserGenerator.getDefaultUser());
        ValidatableResponse responseCreate = userClient.login(credentials);
        actualAccessToken = responseCreate.extract().path("accessToken");

        ValidatableResponse responseGetOrders = orderClient.orders(actualAccessToken);
        getOrders = responseGetOrders.extract().path("orders.number");
        responseGetOrders.assertThat().body("orders.number", is(notNullValue())).and().statusCode(200);
    }

    @Test
    @DisplayName("Получение списка заказов неавторизованным пользователем")
    @Description("Проверка кода ответа и сообщения об ошибке")
    public void getOrdersWithoutAuthUser() {
        ValidatableResponse responseGetOrders = orderClient.orders("");
        int statusCode = responseGetOrders.extract().statusCode();
        String messageError = responseGetOrders.extract().path("message");

        assertEquals(401, statusCode);
        assertEquals(messageError, "You should be authorised");
    }
}
