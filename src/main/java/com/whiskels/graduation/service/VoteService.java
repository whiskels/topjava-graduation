package com.whiskels.graduation.service;

import com.whiskels.graduation.model.Restaurant;
import com.whiskels.graduation.model.User;
import com.whiskels.graduation.model.Vote;
import com.whiskels.graduation.repository.RestaurantRepository;
import com.whiskels.graduation.repository.UserRepository;
import com.whiskels.graduation.repository.VoteRepository;
import com.whiskels.graduation.util.exception.NotFoundException;
import com.whiskels.graduation.util.exception.VoteDeadlineException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.whiskels.graduation.model.Vote.VOTE_DEADLINE;
import static com.whiskels.graduation.util.RepositoryUtil.findById;
import static com.whiskels.graduation.util.ValidationUtil.checkNotFoundWithId;

@Service
public class VoteService {
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    public VoteService(VoteRepository repository, UserRepository userRepository, RestaurantRepository restaurantRepository) {
        this.voteRepository = repository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public Vote vote(int userId, int restaurantId, LocalDateTime localDateTime) {
        Assert.notNull(restaurantId, "userId must not be null");
        Assert.notNull(userId, "restaurantId must not be null");
        try {
            Vote vote = checkNotFoundWithId(voteRepository.getByUserIdAndDate(userId, localDateTime.toLocalDate()), userId);
            if (localDateTime.toLocalTime().isBefore(VOTE_DEADLINE)) {
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
            return voteRepository.save(new Vote(null, localDateTime.toLocalDate(), user, restaurant));
        }
    }

    public void delete(int id, int userId) {
        checkNotFoundWithId(voteRepository.deleteByIdAndUserId(id, userId) != 0, id);
    }

    public Vote getByUserIdAAndDate(int userId, LocalDate date) {
        return checkNotFoundWithId(voteRepository.getByUserIdAndDate(userId, date), userId);
    }

    public List<Vote> getAll(int userId) {
        return voteRepository.getAll(userId);
    }
}
