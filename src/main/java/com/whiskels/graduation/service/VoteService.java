package com.whiskels.graduation.service;

import com.whiskels.graduation.model.Vote;
import com.whiskels.graduation.repository.VoteRepositoryImpl;
import org.springframework.util.Assert;

import java.util.List;

import static com.whiskels.graduation.util.ValidationUtil.checkNotFoundWithId;

public class VoteService {
    private final VoteRepositoryImpl repository;

    public VoteService(VoteRepositoryImpl repository) {
        this.repository = repository;
    }

    public Vote create(Vote vote, int userId, int restaurantId) {
        Assert.notNull(vote, "vote must not be null");
        return repository.save(vote, userId, restaurantId);
    }

    public Vote update(Vote vote, int userId, int restaurantId) {
        Assert.notNull(vote, "dish must not be null");
        return checkNotFoundWithId(repository.save(vote, userId, restaurantId), vote.id());
    }

    public void delete(int id, int userId) {
        checkNotFoundWithId(repository.delete(id, userId), id);
    }

    public Vote get(int id, int userId) {
        return checkNotFoundWithId(repository.get(id, userId), id);
    }

    public List<Vote> getAll(int userId) {
        return repository.getAll(userId);
    }
}
