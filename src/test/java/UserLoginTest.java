import api.user.Credentials;
import api.user.UserClient;
import api.user.UserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

public class UserLoginTest {
    private UserClient userClient;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Авторизация пользователя")
    @Description("Проверка получения токена в теле ответа и кода ответа")
    public void userCanBeLogin() {
        Credentials credentials = Credentials.from(UserGenerator.getDefaultUser());
        ValidatableResponse responseCreate = userClient.login(credentials);
        int actualStatusCode = responseCreate.extract().statusCode();
        String actualAccessToken = responseCreate.extract().path("accessToken");

        assertEquals(200, actualStatusCode);
        assertThat("User token is incorrect", actualAccessToken, containsString("Bearer"));
    }

    @Test
    @DisplayName("Авторизация пользователя с неправильными данными")
    @Description("Проверка сообщения об ошибке и кода ответа")
    public void userCanNotBeLoginWithWrongInput() {
        Credentials credentials = Credentials.from(UserGenerator.getWrongLogPass());
        ValidatableResponse responseCreate = userClient.login(credentials);
        int actualStatusCode = responseCreate.extract().statusCode();
        String messageError = responseCreate.extract().path("message");

        assertEquals(401, actualStatusCode);
        assertEquals(messageError, "email or password are incorrect");
    }
}
