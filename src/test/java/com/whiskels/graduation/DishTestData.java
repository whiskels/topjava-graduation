package com.whiskels.graduation;

import com.whiskels.graduation.model.Dish;

import java.time.LocalDate;

import static com.whiskels.graduation.model.AbstractBaseEntity.START_SEQ;

public class DishTestData {
    public static TestMatcher<Dish> DISH_MATCHER = TestMatcher.usingFieldsComparator("date", "restaurant");

    public static final int DISH_1_ID = START_SEQ + 13;
    public static final int DISH_2_ID = START_SEQ + 14;
    public static final int DISH_3_ID = START_SEQ + 15;

    public static final Dish DISH_1 = new Dish(DISH_1_ID, LocalDate.now(), "Caprese Burger", 500);
    public static final Dish DISH_2 = new Dish(DISH_2_ID, LocalDate.now(), "Borsch", 300);
    public static final Dish DISH_3 = new Dish(DISH_3_ID, LocalDate.now(), "New York Cheesecake", 350);

    public static Dish getNewDish() {
        return new Dish(null, LocalDate.now(), "New", 1000);
    }

    public static Dish getUpdatedDish() {
        Dish updated = new Dish(DISH_1);
        updated.setName("UpdatedName");
        return updated;
    }
}
