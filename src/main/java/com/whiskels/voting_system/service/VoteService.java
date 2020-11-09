package com.whiskels.voting_system.service;

import com.whiskels.voting_system.model.Restaurant;
import com.whiskels.voting_system.model.User;
import com.whiskels.voting_system.model.Vote;
import com.whiskels.voting_system.repository.RestaurantRepository;
import com.whiskels.voting_system.repository.UserRepository;
import com.whiskels.voting_system.repository.VoteRepository;
import com.whiskels.voting_system.util.exception.NotFoundException;
import com.whiskels.voting_system.util.exception.VoteDeadlineException;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.whiskels.voting_system.model.Vote.VOTE_DEADLINE;
import static com.whiskels.voting_system.util.RepositoryUtil.findById;
import static com.whiskels.voting_system.util.ValidationUtil.checkNotFoundWithId;

@Service
@RequiredArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    @Setter
    private Clock clock = Clock.systemDefaultZone();

    @CacheEvict(value = "restaurantTos", allEntries = true)
    @Transactional
    public Vote vote(int userId, int restaurantId) {
        LocalDateTime votingLocalDateTime = LocalDateTime.now(clock);
        final Restaurant restaurant = findById(restaurantRepository, restaurantId);
        final User user = findById(userRepository, userId);
        Vote vote;
        try {
            vote = getByUserIdAndDate(user, votingLocalDateTime.toLocalDate());
        } catch (NotFoundException e) {
            return voteRepository.save(new Vote(null, votingLocalDateTime.toLocalDate(), user, restaurant));
        }

        if (votingLocalDateTime.toLocalTime().isBefore(VOTE_DEADLINE)) {
            if (vote.getRestaurant().id() != restaurantId) {
                vote.setRestaurant(restaurant);
                return voteRepository.save(vote);
            }
            return vote;
        } else {
            throw new VoteDeadlineException("Vote deadline has already passed");
        }
    }

    public Vote getByUserIdAndDate(User user, LocalDate date) {
        return checkNotFoundWithId(voteRepository.getByUserAndLocalDate(user, date), user.id());
    }

    public List<Vote> getAll(int userId) {
        return voteRepository.getAll(userId);
    }
}
