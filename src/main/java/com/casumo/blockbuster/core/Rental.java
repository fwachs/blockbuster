package com.casumo.blockbuster.core;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "rentals")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "rental", cascade = CascadeType.PERSIST)
    private List<RentedFilm> rentedFilms;

    public Rental() {
        rentedFilms = new ArrayList<RentedFilm>();
    }

    public Rental(Customer customer) {
        this.customer = customer;
    }

    public Rental(Customer customer, List<RentedFilm> rentedFilms) {
        this.customer = customer;
        this.rentedFilms = rentedFilms;
    }

    @JsonProperty
    public long calculateInitialPrice() {
        long totalPrice = 0;
        for (RentedFilm rentedFilm : rentedFilms) {
            FilmType type = rentedFilm.getFilm().getType();
            Price price = type.getPrice();

            int amountOfDaysRented = rentedFilm.getDays();

            if (type.getFreeDays() > 0) {
                amountOfDaysRented = amountOfDaysRented - type.getFreeDays();
                totalPrice += price.getAmount();
            }

            if (amountOfDaysRented > 0) {
                totalPrice += price.getAmount() * amountOfDaysRented;
            }
        }
        return totalPrice;
    }

    @JsonProperty
    public long calculateAfterReturnPrice() {
        long totalPrice = 0;

        for (RentedFilm rentedFilm : rentedFilms) {
            long extraDays = rentedFilm.calculateExtraDays();
            if (extraDays > 0) {
                totalPrice += rentedFilm.getFilm().getType().getPrice().getAmount() * extraDays;
            }
        }

        return totalPrice;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<RentedFilm> getRentedFilms() {
        return rentedFilms;
    }

    public void setRentedFilms(List<RentedFilm> rentedFilms) {
        this.rentedFilms = rentedFilms;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

}
