package com.whiskels.graduation.web.vote;

import com.whiskels.graduation.service.VoteService;
import com.whiskels.graduation.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.whiskels.graduation.RestaurantTestData.RESTAURANT_1_ID;
import static com.whiskels.graduation.TestUtil.userHttpBasic;
import static com.whiskels.graduation.UserTestData.USER;
import static com.whiskels.graduation.web.vote.VoteController.REST_URL;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VoteRestControllerTest  extends AbstractControllerTest {

    @Autowired
    private VoteService voteService;

    @Test
    void voteSimple() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", String.valueOf(RESTAURANT_1_ID))
                .with(userHttpBasic(USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(status().isCreated());
    }
}
