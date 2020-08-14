package com.whiskels.graduation.repository;

import com.whiskels.graduation.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Integer> {
    Vote findOneByIdAndUserId(int id, int userId);

    List<Vote> findAllByUserIdOrderByDateDesc(int userId);

    List<Vote> findAllByRestaurantIdAndDate(int restaurantId, LocalDate date);

    @Transactional
    int deleteByIdAndUserId(int id, int userId);

    @Query("SELECT v FROM Vote v JOIN FETCH v.user WHERE v.id=:id AND v.restaurant.id=:restaurantId")
    Vote getWithRestaurant(@Param("id") int id, @Param("restaurantId") int restaurantId);

}
