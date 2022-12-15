import api.user.User;
import api.user.UserClient;
import api.user.UserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserCreatePositiveTest {
    private UserClient userClient;
    private String actualAccessToken;

    public UserCreatePositiveTest() {

    }

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Регистрация нового пользователя")
    @Description("Проверка успешной регистрации и кода ответа")
    public void userCanBeCreated() {
        User user = UserGenerator.getRandomUser();
        ValidatableResponse responseCreate = userClient.create(user);
        int actualStatusCode = responseCreate.extract().statusCode();
        boolean isUserCreated = responseCreate.extract().path("success");
        actualAccessToken = responseCreate.extract().path("accessToken");

        assertEquals(200, actualStatusCode);
        assertTrue(isUserCreated);
    }

    @After
    public void tearDown() {
        userClient.delete(actualAccessToken);
    }
}
