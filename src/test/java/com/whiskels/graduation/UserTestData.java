package com.whiskels.graduation;

import com.whiskels.graduation.model.Role;
import com.whiskels.graduation.model.User;
import com.whiskels.graduation.web.json.JsonUtil;

import java.util.Collections;
import java.util.Date;

import static com.whiskels.graduation.VoteTestData.VOTES;
import static com.whiskels.graduation.model.AbstractBaseEntity.START_SEQ;

public class UserTestData {
    public static TestMatcher<User> USER_MATCHER = TestMatcher.usingFieldsWithIgnoringAssertions(User.class, "registered", "votes", "password");

    public static final int NOT_FOUND = 10;
    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;

    public static final User USER = new User(USER_ID, "User", "user@yandex.ru", "password", Role.USER);
    public static final User ADMIN = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", Role.ADMIN);

    static {
        USER.setVotes(VOTES);
    }

    public static User getNew() {
        return new User(null, "New", "new@gmail.com", "newPass", false, new Date(), Collections.singleton(Role.USER));
    }

    public static User getUpdatedUser() {
        User updated = new User(USER);
        updated.setName("UpdatedName");
        return updated;
    }

    public static String jsonWithPassword(User user, String passw) {
        return JsonUtil.writeAdditionProps(user, "password", passw);
    }
}
