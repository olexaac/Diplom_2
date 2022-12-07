import api.user.User;
import api.user.UserClient;
import api.user.UserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserForUpdateTest {
    private UserClient userClient;
    private String actualAccessToken;

    public UserForUpdateTest() {

    }

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Изменение данных у авторизованного пользователя: e-mail, пароль, имя")
    @Description("Проверка статуса о успешном изменении и тела ответа с учетными данными")
    public void userUpdateValuesForAuthorization() {
        User user = UserGenerator.getRandomUser();
        ValidatableResponse responseCreate = userClient.create(user);
        actualAccessToken = responseCreate.extract().path("accessToken");

        ValidatableResponse responseUpdate = userClient.update(actualAccessToken);
        responseUpdate.assertThat().body("success", equalTo(true)).and().statusCode(200);
        responseUpdate.assertThat().body("user.email",  containsString("@changed.test")).and().body("user.name", equalTo("Changed"));
    }

    @Test
    @DisplayName("Изменение данных у неавторизованного пользователя: e-mail, пароль, имя")
    @Description("Проверка статуса о неуспешном изменении и сообщения об ошибке")
    public void userUpdateValuesForNotAuthorization() {
        User user = UserGenerator.getRandomUser();
        ValidatableResponse responseCreate = userClient.create(user);
        actualAccessToken = responseCreate.extract().path("accessToken");

        ValidatableResponse responseUpdate = userClient.update("");
        responseUpdate.assertThat().body("success", equalTo(false)).and().body("message", equalTo("You should be authorised")).and().statusCode(401);
    }

    @After
    public void tearDown() {
        userClient.delete(actualAccessToken);
    }
}
