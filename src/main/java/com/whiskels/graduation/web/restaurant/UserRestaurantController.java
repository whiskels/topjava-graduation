package com.whiskels.graduation.web.restaurant;

import com.whiskels.graduation.model.Dish;
import com.whiskels.graduation.model.Restaurant;
import com.whiskels.graduation.service.DishService;
import com.whiskels.graduation.service.RestaurantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = UserRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserRestaurantController {

    public static final String REST_URL = "/restaurants";

    public static final String DISHES_REST_URL = "/{restaurantId}/dishes";

    private final RestaurantService restaurantService;

    private final DishService dishService;

    public UserRestaurantController(RestaurantService restaurantService, DishService dishService) {
        this.restaurantService = restaurantService;
        this.dishService = dishService;
    }

    @GetMapping
    public List<Restaurant> getAll() {
        log.info("getAll");
        return restaurantService.getAllByDishesDate(LocalDate.now());
    }

    @GetMapping(value = REST_URL + DISHES_REST_URL + "/{localDate}")
    public List<Dish> getDishesByDate(@PathVariable int restaurantId, @PathVariable LocalDate localDate) {
        log.info("get by date {}", localDate);
        return dishService.getByDate(localDate, restaurantId);
    }
}
