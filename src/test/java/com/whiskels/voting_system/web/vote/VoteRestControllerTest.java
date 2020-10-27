package com.whiskels.voting_system.web.vote;

import com.whiskels.voting_system.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.whiskels.voting_system.RestaurantTestData.RESTAURANT_1_ID;
import static com.whiskels.voting_system.TestUtil.userHttpBasic;
import static com.whiskels.voting_system.UserTestData.USER;
import static com.whiskels.voting_system.web.vote.VoteController.REST_URL;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VoteRestControllerTest  extends AbstractControllerTest {

    @Test
    void voteSimple() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", String.valueOf(RESTAURANT_1_ID))
                .with(userHttpBasic(USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(status().isCreated());
    }
}
