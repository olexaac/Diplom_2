import api.user.User;
import api.user.UserClient;
import api.user.UserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserCreateNegativeTest {

    private UserClient userClient;

    public UserCreateNegativeTest() {

    }

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Регистрация существующего пользователя")
    @Description("Проверка кода ответа и сообщения об ошибке")
    public void userCanNotBeCreatedIfHeWasCreated() {
        User user = UserGenerator.getDefaultUser();
        ValidatableResponse responseCreate = userClient.create(user);
        int actualStatusCode = responseCreate.extract().statusCode();
        String messageError = responseCreate.extract().path("message");

        assertEquals(403, actualStatusCode);
        assertEquals(messageError, "User already exists");
    }

    @Test
    @DisplayName("Регистрация пользователя без заполнения обязательных полей")
    @Description("Проверка кода ответа и сообщения об ошибке")
    public void userCanNotBeCreatedWithEmptyInput() {
        User user = UserGenerator.getNotInput();
        ValidatableResponse responseCreate = userClient.create(user);
        int actualStatusCode = responseCreate.extract().statusCode();
        String messageError = responseCreate.extract().path("message");

        assertEquals(403, actualStatusCode);
        assertEquals(messageError, "Email, password and name are required fields");
    }
}
