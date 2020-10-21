package com.whiskels.graduation.service;

import com.whiskels.graduation.model.Vote;
import com.whiskels.graduation.util.exception.NotFoundException;
import com.whiskels.graduation.util.exception.VoteDeadlineException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Clock;
import java.util.List;

import static com.whiskels.graduation.DishTestData.DISH_1_ID;
import static com.whiskels.graduation.UserTestData.*;
import static com.whiskels.graduation.VoteTestData.*;
import static com.whiskels.graduation.model.Vote.VOTE_DEADLINE;
import static com.whiskels.graduation.util.DateTimeUtil.createClock;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VoteServiceTest extends AbstractServiceTest {
    @Autowired
    protected VoteService voteService;

    @Test
    void voteSimple() throws Exception {
        Vote newVote = getNewVote();
        Vote created = voteService.vote(newVote.getUser().id(), newVote.getRestaurant().id());
        newVote.setId(created.getId());
        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(voteService.getByUserIdAAndDate(created.getUser().getId(), created.getDate()), newVote);
    }

    @Test
    void voteAgainBeforeDeadline() throws Exception {
        Clock clock = createClock(VOTE_3.getDate(), VOTE_DEADLINE.minusMinutes(1));
        voteService.setClock(clock);
        Vote newVote = getNewVote();
        Vote created = voteService.vote(newVote.getUser().id(), newVote.getRestaurant().id());
        newVote.setId(created.getId());
        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(voteService.getByUserIdAAndDate(created.getUser().getId(), created.getDate()), newVote);}

    @Test
    void voteAgainAfterDeadline() throws Exception {
        Clock clock = createClock(VOTE_3.getDate(), VOTE_DEADLINE);
        voteService.setClock(clock);
        Vote newVote = getNewVote();
        validateRootCause(() ->voteService.vote(newVote.getUser().id(), newVote.getRestaurant().id()), VoteDeadlineException.class);
    }

    @Test
    void getAll() throws Exception {
        List<Vote> voteList = voteService.getAll(USER_ID);
        VOTE_LAZY_MATCHER.assertMatch(voteList, VOTE_3, VOTE_2, VOTE_1);
    }

    @Test
    void delete() throws Exception {
        voteService.delete(VOTE_1_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> voteService.getByUserIdAAndDate(USER_ID, VOTE_1.getDate()));
    }

    @Test
    void deleteNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> voteService.delete(NOT_FOUND, USER_ID));
    }

    @Test
    void deleteNotOwn() throws Exception {
        assertThrows(NotFoundException.class, () -> voteService.delete(DISH_1_ID, ADMIN_ID));
    }
}
