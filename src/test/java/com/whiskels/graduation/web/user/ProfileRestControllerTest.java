package com.whiskels.graduation.web.user;

import com.whiskels.graduation.model.User;
import com.whiskels.graduation.service.UserService;
import com.whiskels.graduation.to.UserTo;
import com.whiskels.graduation.util.UserUtil;
import com.whiskels.graduation.web.AbstractControllerTest;
import com.whiskels.graduation.web.json.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.whiskels.graduation.TestUtil.readFromJson;
import static com.whiskels.graduation.TestUtil.userHttpBasic;
import static com.whiskels.graduation.UserTestData.*;
import static com.whiskels.graduation.web.user.ProfileRestController.REST_URL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfileRestControllerTest extends AbstractControllerTest {
    @Autowired
    private UserService userService;

    @Test
    void test() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk());

        User user = readFromJson(resultActions, User.class);
        System.out.println(user.toString());
    }


    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(USER));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL)
                .with(userHttpBasic(USER)))
                .andExpect(status().isNoContent());
        USER_MATCHER.assertMatch(userService.getAll(), ADMIN);
    }

    @Test
    void register() throws Exception {
        UserTo newTo = new UserTo(null, "newName", "newemail@ya.ru", "newPassword");
        User newUser = UserUtil.createNewFromTo(newTo);
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isCreated());

        User created = readFromJson(action, User.class);
        int newId = created.getId();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(userService.get(newId), newUser);
    }

    @Test
    void update() throws Exception {
        UserTo updatedTo = new UserTo(null, "newName", "user@yandex.ru", "newPassword");
        perform(MockMvcRequestBuilders.put(REST_URL).contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(USER))
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isNoContent());

        USER_MATCHER.assertMatch(userService.get(USER_ID), UserUtil.updateFromTo(new User(USER), updatedTo));
    }

    @Test
    void getWithVotes() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/with-votes")
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(USER));
    }

//    @Test
//    void updateInvalid() throws Exception {
//        UserTo updatedTo = new UserTo(null, null, "password", null);
//        perform(MockMvcRequestBuilders.put(REST_URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .with(userHttpBasic(USER))
//                .content(JsonUtil.writeValue(updatedTo)))
//                .andDo(print())
//                .andExpect(status().isUnprocessableEntity())
//                .andExpect(errorType(VALIDATION_ERROR));
//    }
//
//    @Test
//    @Transactional(propagation = Propagation.NEVER)
//    void updateDuplicate() throws Exception {
//        UserTo updatedTo = new UserTo(null, "newName", "admin@gmail.com", "newPassword");
//        perform(MockMvcRequestBuilders.put(REST_URL).contentType(MediaType.APPLICATION_JSON)
//                .with(userHttpBasic(USER))
//                .content(JsonUtil.writeValue(updatedTo)))
//                .andDo(print())
//                .andExpect(status().isUnprocessableEntity())
//                .andExpect(errorType(VALIDATION_ERROR))
//                .andExpect(detailMessage(EXCEPTION_DUPLICATE_EMAIL));
//    }
}
