package com.whiskels.graduation.service;

import com.whiskels.graduation.model.Restaurant;
import com.whiskels.graduation.model.User;
import com.whiskels.graduation.model.Vote;
import com.whiskels.graduation.repository.RestaurantRepository;
import com.whiskels.graduation.repository.UserRepository;
import com.whiskels.graduation.repository.VoteRepository;
import com.whiskels.graduation.util.exception.NotFoundException;
import com.whiskels.graduation.util.exception.VoteDeadlineException;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.whiskels.graduation.model.Vote.VOTE_DEADLINE;
import static com.whiskels.graduation.util.RepositoryUtil.findById;
import static com.whiskels.graduation.util.ValidationUtil.checkNotFoundWithId;

@Service
@RequiredArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    @Setter
    private Clock clock = Clock.systemDefaultZone();

    public Vote vote(int userId, int restaurantId) {
        Assert.notNull(restaurantId, "userId must not be null");
        Assert.notNull(userId, "restaurantId must not be null");
        LocalDateTime votingLocalDateTime = LocalDateTime.now(clock);
        try {
            Vote vote = checkNotFoundWithId(voteRepository.getByUserIdAndDate(userId, votingLocalDateTime.toLocalDate()), userId);
            if (votingLocalDateTime.toLocalTime().isBefore(VOTE_DEADLINE)) {
                if (vote.getRestaurant().id() != restaurantId) {
                    vote.setRestaurant(findById(restaurantRepository, restaurantId));
                    return voteRepository.save(vote);
                }
                return vote;
            } else {
                throw new VoteDeadlineException("Vote deadline has already passed");
            }
        } catch (NotFoundException e) {
            final Restaurant restaurant = findById(restaurantRepository, restaurantId);
            final User user = findById(userRepository, userId);
            return voteRepository.save(new Vote(null, votingLocalDateTime.toLocalDate(), user, restaurant));
        }
    }

    public Vote getByUserIdAndDate(int userId, LocalDate date) {
        return checkNotFoundWithId(voteRepository.getByUserIdAndDate(userId, date), userId);
    }

    public List<Vote> getAll(int userId) {
        return voteRepository.getAll(userId);
    }
}
