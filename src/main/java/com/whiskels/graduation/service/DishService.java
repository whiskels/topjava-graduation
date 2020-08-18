package com.whiskels.graduation.service;

import com.whiskels.graduation.model.Dish;
import com.whiskels.graduation.repository.DishRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;

import static com.whiskels.graduation.util.ValidationUtil.checkNotFoundWithId;

@Service
public class DishService {
    private static final Sort SORT_NAME_PRICE = Sort.by(Sort.Direction.ASC, "name", "price");

    private final DishRepository repository;

    public DishService(DishRepository repository) {
        this.repository = repository;
    }

    public Dish create(Dish dish) {
        Assert.notNull(dish, "dish must not be null");
        return repository.save(dish);
    }

    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id) != 0, id);
    }

    public void deleteAllByRestaurantId(int id) {
        checkNotFoundWithId(repository.deleteAllByRestaurantId(id) != 0, id);
    }

    public Dish get(int id) {
        return checkNotFoundWithId(repository.findById(id).orElse(null), id);
    }

    public List<Dish> getAll() {
        return repository.findAll(SORT_NAME_PRICE);
    }

    public List<Dish> getAllByRestaurantIdAndDate(int id, LocalDate date) {
        return repository.getAllByRestaurantIdAndDate(id, date, SORT_NAME_PRICE);
    }

    public void update(Dish dish) {
        Assert.notNull(dish, "dish must not be null");
        checkNotFoundWithId(repository.save(dish), dish.id());
    }
}
