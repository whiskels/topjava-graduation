package com.whiskels.voting_system.web.restaurant;

import com.whiskels.voting_system.model.Restaurant;
import com.whiskels.voting_system.service.DishService;
import com.whiskels.voting_system.service.RestaurantService;
import com.whiskels.voting_system.util.exception.NotFoundException;
import com.whiskels.voting_system.web.AbstractControllerTest;
import com.whiskels.voting_system.web.json.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.whiskels.voting_system.RestaurantTestData.*;
import static com.whiskels.voting_system.TestUtil.readFromJson;
import static com.whiskels.voting_system.TestUtil.userHttpBasic;
import static com.whiskels.voting_system.UserTestData.ADMIN;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminRestaurantRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = AdminRestaurantController.REST_URL + '/';

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    DishService dishService;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT_1_ID)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(RESTAURANT_1));
    }

    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT_1_ID))
                .andExpect(status().isUnauthorized());
    }

//    @Test
//    void getNotFound() throws Exception {
//        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND)
//                .with(userHttpBasic(ADMIN)))
//                .andDo(print())
//                .andExpect(status().isUnprocessableEntity());
//    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + RESTAURANT_1_ID)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> restaurantService.get(RESTAURANT_1_ID));
    }

//    @Test
//    void deleteNotFound() throws Exception {
//        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND)
//                .with(userHttpBasic(ADMIN)))
//                .andExpect(status().isUnprocessableEntity());
//    }

    @Test
    void create() throws Exception {
        Restaurant newRestaurant = getNewRestaurant();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newRestaurant)))
                .andDo(print())
                .andExpect(status().isCreated());

        Restaurant created = readFromJson(action, Restaurant.class);
        int newId = created.getId();
        newRestaurant.setId(newId);
        RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
        RESTAURANT_MATCHER.assertMatch(restaurantService.get(newId), newRestaurant);
    }

    @Test
    void update() throws Exception {
        Restaurant updated = getUpdatedRestaurant();
        perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        RESTAURANT_MATCHER.assertMatch(restaurantService.get(RESTAURANT_1_ID), updated);
    }

    @Test
    void enable() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL + RESTAURANT_1_ID)
                .param("enabled", "false")
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertFalse(restaurantService.get(RESTAURANT_1_ID).isEnabled());
    }

//    @Test
//    void createDishWithLocation() throws Exception {
//        Dish newDish = getNewDish();
//        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL + RESTAURANT_1_ID + "/dishes")
//                .with(userHttpBasic(ADMIN))
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(JsonUtil.writeValue(newDish)))
//                .andDo(print());
//
//        Dish created = readFromJson(action, Dish.class);
//        int newId = created.getId();
//        newDish.setId(newId);
//        DISH_MATCHER.assertMatch(created, newDish);
//        DISH_MATCHER.assertMatch(dishService.get(newId, RESTAURANT_1_ID), newDish);
//    }

    @Test
    void updateDish() throws Exception {
    }

    @Test
    void deleteDish() throws Exception {
    }

    @Test
    void getDish() throws Exception {
    }
}
