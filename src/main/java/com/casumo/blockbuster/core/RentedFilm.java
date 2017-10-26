package com.casumo.blockbuster.core;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.casumo.blockbuster.exception.AlreadyReturnedException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "rented_films")
public class RentedFilm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "film_id", nullable = false)
    private Film film;

    @ManyToOne
    @JoinColumn(name = "rental_id")
    private Rental rental;
    private Date createdOn;
    private Date returnedOn;
    private int days;

    public RentedFilm() {
    }

    public RentedFilm(Film film, int days) {
        this.film = film;
        this.days = days;
        this.createdOn = new Date();
    }

    @JsonProperty
    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    @JsonIgnore
    public Rental getRental() {
        return rental;
    }

    public void setRental(Rental rental) {
        this.rental = rental;
    }

    @JsonProperty
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @JsonProperty
    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    @JsonProperty
    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @JsonProperty
    public Date getReturnedOn() {
        return returnedOn;
    }

    public void setReturnedOn(Date returnedOn) {
        this.returnedOn = returnedOn;
    }

    public void markAsReturned() throws AlreadyReturnedException {
        if (this.returnedOn != null) {
            throw new AlreadyReturnedException("Already returned movie id: " + this.getId());
        }
        this.getFilm().increaseStock();
        setReturnedOn(new Date());
    }

    public long calculateExtraDays() {
        if (this.returnedOn == null) {
            return 0;
        }

        long diffTime = this.returnedOn.getTime() - this.createdOn.getTime();
        long diffDays = diffTime / (1000 * 60 * 60 * 24);

        if (diffDays > this.days) {
            return diffDays - this.days;
        }
        return 0;
    }

}
