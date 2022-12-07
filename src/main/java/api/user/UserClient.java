package api.user;

import api.base.Client;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserClient extends Client {

    private static final String PATH_CREATE = "api/auth/register/";

    private static final String PATH_LOGIN = "api/auth/login";

    private static final String PATH_DELETE_CHANGE = "api/auth/user";

    @Step("Запрос на регистрацию пользователя")
    public ValidatableResponse create(User user) {
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .post(PATH_CREATE)
                .then();
    }

    @Step("Запрос на авторизацию пользователя")
    public ValidatableResponse login(Credentials credentials) {
        return given()
                .spec(getSpec())
                .body(credentials)
                .when()
                .post(PATH_LOGIN)
                .then();
    }

    @Step("Запрос на удаление пользователя")
    public ValidatableResponse delete(String actualAccessToken) {
        return given()
                .spec(getSpec())
                .header("authorization", actualAccessToken)
                .when()
                .delete(PATH_DELETE_CHANGE)
                .then()
                .assertThat()
                .statusCode(202)
                .and()
                .body("message", equalTo("User successfully removed"));
    }

    @Step("Запрос на обновление данных пользователя")
    public ValidatableResponse update(String actualAccessToken) {
        User user = new User(RandomStringUtils.randomAlphabetic(10) + "@changed.test", "12345", "Changed");
        return given()
                .spec(getSpec())
                .header("authorization", actualAccessToken)
                .body(user)
                .when()
                .patch(PATH_DELETE_CHANGE)
                .then();
    }
}
