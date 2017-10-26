package com.casumo.blockbuster.core;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private long id;
    private String name;

    public Customer() {
    }

    public Customer(long id) {
        this.id = id;
    }

    public Customer(long id, String name) {
        this(id);
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Customer rhs = (Customer) obj;
        return new EqualsBuilder().appendSuper(super.equals(obj)).append(id, rhs.id).isEquals();
    }

    public int calculateBonusPoints(List<Rental> rentals) {
        int bonusPoints = 0;
        for (Rental rental : rentals) {
            for (RentedFilm rentedFilm : rental.getRentedFilms()) {
                bonusPoints += rentedFilm.getFilm().getType().getBonusPoints();
            }
        }
        return bonusPoints;
    }
}
