package com.casumo.blockbuster.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "film_type_prices")
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private long id;
    private long amount;
    private String code;

    public Price() {
    }

    public Price(long id, long amount, String code) {
        this.id = id;
        this.amount = amount;
        this.code = code;
    }

    @JsonProperty
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @JsonProperty
    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    @JsonProperty
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
