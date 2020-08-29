package com.whiskels.graduation.service;

import com.whiskels.graduation.model.Dish;
import com.whiskels.graduation.repository.DishRepositoryImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;

import static com.whiskels.graduation.util.ValidationUtil.checkNotFoundWithId;

@Service
public class DishService {
    private final DishRepositoryImpl repository;

    public DishService(DishRepositoryImpl dishRepository) {
        this.repository = dishRepository;
    }

    public Dish create(Dish dish, int restaurantId) {
        Assert.notNull(dish, "dish must not be null");
        return repository.save(dish, restaurantId);
    }

    public Dish update(Dish dish, int restaurantId) {
        Assert.notNull(dish, "dish must not be null");
        return checkNotFoundWithId(repository.save(dish, restaurantId), dish.id());
    }

    public void delete(int id, int restaurantId) {
        checkNotFoundWithId(repository.delete(id, restaurantId), id);
    }

    public Dish get(int id, int restaurantId) {
        return checkNotFoundWithId(repository.get(id, restaurantId), id);
    }

    public List<Dish> getAll(int restaurantId) {
        return repository.getAll(restaurantId);
    }

    public List<Dish> getByDate(LocalDate date, int restaurantId) {
        return repository.getByDate(date, restaurantId);
    }
}
