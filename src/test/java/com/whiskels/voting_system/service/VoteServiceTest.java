package com.whiskels.voting_system.service;

import com.whiskels.voting_system.model.Vote;
import com.whiskels.voting_system.util.exception.VoteDeadlineException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Clock;
import java.util.List;

import static com.whiskels.voting_system.UserTestData.USER_ID;
import static com.whiskels.voting_system.VoteTestData.*;
import static com.whiskels.voting_system.model.Vote.VOTE_DEADLINE;
import static com.whiskels.voting_system.util.DateTimeUtil.createClock;

class VoteServiceTest extends AbstractServiceTest {
    @Autowired
    protected VoteService voteService;

    @Test
    void voteSimple() throws Exception {
        Vote newVote = getNewVote();
        Vote created = voteService.vote(newVote.getUser().id(), newVote.getRestaurant().id());
        newVote.setId(created.getId());
        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(voteService.getByUserIdAndDate(created.getUser(), created.getLocalDate()), newVote);
    }

    @Test
    void voteAgainBeforeDeadline() throws Exception {
        Clock clock = createClock(VOTE_3.getLocalDate(), VOTE_DEADLINE.minusMinutes(1));
        voteService.setClock(clock);
        Vote newVote = getNewVote();
        Vote created = voteService.vote(newVote.getUser().id(), newVote.getRestaurant().id());
        newVote.setId(created.getId());
        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(voteService.getByUserIdAndDate(created.getUser(), created.getLocalDate()), newVote);}

    @Test
    void voteAgainAfterDeadline() throws Exception {
        Clock clock = createClock(VOTE_3.getLocalDate(), VOTE_DEADLINE);
        voteService.setClock(clock);
        Vote newVote = getNewVote();
        validateRootCause(() ->voteService.vote(newVote.getUser().id(), newVote.getRestaurant().id()), VoteDeadlineException.class);
    }

    @Test
    void getAll() throws Exception {
        List<Vote> voteList = voteService.getAll(USER_ID);
        VOTE_LAZY_MATCHER.assertMatch(voteList, VOTE_3, VOTE_2, VOTE_1);
    }
}
