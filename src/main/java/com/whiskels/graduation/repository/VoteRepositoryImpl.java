package com.whiskels.graduation.repository;

import com.whiskels.graduation.model.Vote;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class VoteRepositoryImpl {
    private VoteRepository voteRepository;
    private UserRepository userRepository;
    private RestaurantRepository restaurantRepository;

    public VoteRepositoryImpl(VoteRepository voteRepository, UserRepository userRepository, RestaurantRepository restaurantRepository) {
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional
    public Vote save(Vote vote, int userId, int restaurantId) {
        if (!vote.isNew() && get(vote.getId(), restaurantId) == null) {
            return null;
        }
        vote.setRestaurant(restaurantRepository.getOne(restaurantId));
        vote.setUser(userRepository.getOne(userId));
        return voteRepository.save(vote);
    }

    public boolean delete(int id, int userId) {
        return voteRepository.deleteByIdAndUserId(id, userId) != 0;
    }

    public Vote get(int id, int userId) {
        return voteRepository.findById(id)
                .filter(vote -> vote.getUser().getId() == userId)
                .orElse(null);
    }

    public List<Vote> getAll(int userId) {
        return voteRepository.getAll(userId);
    }
}
