package com.whiskels.graduation.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

import static javax.persistence.FetchType.LAZY;
import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@NamedQueries({
        @NamedQuery(name = Vote.ALL_SORTED, query = "SELECT v FROM Vote v WHERE v.user.id=:userId ORDER BY v.date DESC"),
        @NamedQuery(name = Vote.DELETE, query = "DELETE FROM Vote v WHERE v.id=:id AND v.user.id=:userId"),
        @NamedQuery(name = Vote.GET_BETWEEN, query = "SELECT v FROM Vote v " +
                "WHERE v.user.id=:userId AND v.date >= :startDate AND v.date < :endDate ORDER BY v.date DESC"),
})
@Entity
@Table(name = "votes", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "date"}, name = "votes_unique_user_date_idx")})
@Getter
@Setter
public class Vote extends AbstractBaseEntity {
    public static final String ALL_SORTED = "Vote.getAll";
    public static final String DELETE = "Vote.delete";
    public static final String GET_BETWEEN = "Vote.getBetween";
    public static final LocalTime VOTE_UPDATABLE_BEFORE = LocalTime.of(11, 00);


    @Column(name = "date", nullable = false)
    @NotNull
    private LocalDate date;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = CASCADE)
    @NotNull
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = CASCADE)
    @NotNull
    private Restaurant restaurant;


    public Vote() {
    }

    public Vote(LocalDate date, Restaurant restaurant) {
        this(null, date, restaurant);
    }

    public Vote(Integer id, LocalDate date, Restaurant restaurant) {
        super(id);
        this.date = date;
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "date=" + date +
                ", user=" + user +
                ", restaurant=" + restaurant +
                '}';
    }
}

