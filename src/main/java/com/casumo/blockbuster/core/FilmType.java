package com.casumo.blockbuster.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "film_types")
public class FilmType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "price_id", nullable = false)
    private Price price;
    private String code;
    private int bonusPoints;
    private int freeDays;

    public FilmType() {
    }

    public FilmType(long id, Price price, String code, int bonusPoints, int freeDays) {
        this.id = id;
        this.price = price;
        this.code = code;
        this.bonusPoints = bonusPoints;
        this.freeDays = freeDays;
    }

    @JsonProperty
    public int getFreeDays() {
        return freeDays;
    }

    public void setFreeDays(int freeDays) {
        this.freeDays = freeDays;
    }

    @JsonProperty
    public int getBonusPoints() {
        return bonusPoints;
    }

    public void setBonusPoints(int bonusPoints) {
        this.bonusPoints = bonusPoints;
    }

    @JsonProperty
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @JsonProperty
    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    @JsonProperty
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
