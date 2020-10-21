package com.whiskels.graduation.service;

import com.whiskels.graduation.model.Restaurant;
import com.whiskels.graduation.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;

import static com.whiskels.graduation.util.RepositoryUtil.findById;
import static com.whiskels.graduation.util.ValidationUtil.checkNotFoundWithId;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private static final Sort SORT_NAME_REGISTERED = Sort.by(Sort.Direction.ASC, "name", "registered");

    private final RestaurantRepository repository;

    @CacheEvict(value = "restaurants", allEntries = true)
    public Restaurant create(Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant must not be null");
        return repository.save(restaurant);
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id) != 0, id);
    }

    public Restaurant get(int id) {
        return findById(repository, id);
    }

    @Cacheable("restaurants")
    public List<Restaurant> getAll() {
        return repository.findAll(SORT_NAME_REGISTERED);
    }

    public List<Restaurant> getAllByDishesDate(LocalDate date) {return repository.getAllByDishesDate(date);}

    @CacheEvict(value = "users", allEntries = true)
    public void update(Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant must not be null");
        checkNotFoundWithId(repository.save(restaurant), restaurant.id());
    }

    public Restaurant getWithVotes(int id) {
        return checkNotFoundWithId(repository.getWithVotes(id), id);
    }
}
