package com.whiskels.graduation.service;

import com.whiskels.graduation.model.Dish;
import com.whiskels.graduation.repository.DishRepository;
import com.whiskels.graduation.repository.RestaurantRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;

import static com.whiskels.graduation.util.RepositoryUtil.findById;
import static com.whiskels.graduation.util.ValidationUtil.checkNew;
import static com.whiskels.graduation.util.ValidationUtil.checkNotFoundWithId;

@Service
public class DishService {
    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;

    public DishService(DishRepository dishRepository, RestaurantRepository restaurantRepository) {
        this.dishRepository = dishRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)
    public Dish create(Dish dish, int restaurantId) {
        Assert.notNull(dish, "dish must not be null");
        checkNew(dish);
        dish.setRestaurant(findById(restaurantRepository, restaurantId));
        return dishRepository.save(dish);
    }

    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)
    public Dish update(Dish dish, int restaurantId) {
        Assert.notNull(dish, "dish must not be null");
        checkNotFoundWithId(get(dish.id(), restaurantId), dish.id());
        dish.setRestaurant(findById(restaurantRepository, restaurantId));
        return checkNotFoundWithId(dishRepository.save(dish), dish.id());
    }

    public void delete(int id, int restaurantId) {
        checkNotFoundWithId(dishRepository.delete(id, restaurantId) != 0, id);
    }

    public Dish get(int id, int restaurantId) {
        return checkNotFoundWithId(dishRepository.findById(id)
                .filter(dish -> dish.getRestaurant().getId() == restaurantId)
                .orElse(null), id);
    }

    public List<Dish> getAll(int restaurantId) {
        return dishRepository.getAll(restaurantId);
    }

    public List<Dish> getByDate(LocalDate date, int restaurantId) {
        return dishRepository.getByDate(date, restaurantId);
    }
}