package api.user;

import org.apache.commons.lang3.RandomStringUtils;

public class UserGenerator {

    public static User getRandomUser() {
        final String userEmail = RandomStringUtils.randomAlphabetic(7);
        final String userPassword = RandomStringUtils.randomAlphabetic(7);
        final String userFirstName = RandomStringUtils.randomAlphabetic(7);

        return new User(userEmail + "@stellarburgers.test", userPassword, userFirstName);
    }

    public static User getDefaultUser() {
        return new User("meoaebp@test.test", "hCAfcRP", "wVLHpnr");
    }

    public static User getNotInput() {
        return new User("meoaebp@test.test", null, null);
    }

    public static User getWrongLogPass() {
        return new User("error@test.test", "error", "wVLHpnr");
    }
}
