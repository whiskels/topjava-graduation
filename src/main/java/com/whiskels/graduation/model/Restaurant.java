package com.whiskels.graduation.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.whiskels.graduation.HasId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

import static javax.persistence.FetchType.LAZY;

@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@Table(name = "restaurants", uniqueConstraints = {@UniqueConstraint(columnNames = "name", name = "restaurants_unique_name_idx")})
@Getter
@Setter
@NoArgsConstructor
public class Restaurant extends AbstractNamedEntity implements HasId {
    @Column(name = "enabled", nullable = false, columnDefinition = "bool default true")
    private boolean enabled = true;

    @Column(name = "registered", nullable = false, columnDefinition = "timestamp default now()")
    @NotNull
    private Date registered = new Date();

    @OneToMany(fetch = LAZY, mappedBy = "restaurant")
    @OrderBy("date DESC")
    @JsonManagedReference(value = "restaurantDishes")
    private List<Dish> dishes;

    public Restaurant(Integer id, String name, boolean enabled, Date registered) {
        super(id, name);
        this.enabled = enabled;
        this.registered = registered;
    }

    public Restaurant(Integer id, String name) {
        this(id, name, true, new Date());
    }

    public Restaurant(Restaurant restaurant) {
        this(restaurant.getId(), restaurant.getName(), restaurant.isEnabled(), restaurant.getRegistered());
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + "'" +
                ", enabled=" + enabled +
                '}';
    }
}
