package com.whiskels.voting_system.web.restaurant;

import com.whiskels.voting_system.service.RestaurantService;
import com.whiskels.voting_system.to.RestaurantTo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = UserRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserRestaurantController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    public static final String REST_URL = "/restaurants";

    private final RestaurantService restaurantService;

    public UserRestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public List<RestaurantTo> getAll() {
        return getAllByDate(LocalDate.now());
    }

    @GetMapping(value = REST_URL + "/{localDate}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<RestaurantTo> getAllByDate(@PathVariable LocalDate localDate) {
        log.info("get by date {}", localDate);
        return restaurantService.getAllByDishesDate(localDate);
    }
}
