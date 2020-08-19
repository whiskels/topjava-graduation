package com.whiskels.graduation.service;

import com.whiskels.graduation.model.Dish;
import com.whiskels.graduation.repository.DishRepository;
import com.whiskels.graduation.repository.RestaurantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;

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
    public Dish save(Dish dish, int restaurantId) {
        if (!dish.isNew() && get(dish.getId(), restaurantId) == null) {
            return null;
        }
        dish.setRestaurant(restaurantRepository.getOne(restaurantId));
        return dishRepository.save(dish);
    }

    public Dish create(Dish dish, int restaurantId) {
        Assert.notNull(dish, "dish must not be null");
        return save(dish, restaurantId);
    }

    public Dish update(Dish dish, int restaurantId) {
        Assert.notNull(dish, "dish must not be null");
        return checkNotFoundWithId(save(dish, restaurantId), dish.id());
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
