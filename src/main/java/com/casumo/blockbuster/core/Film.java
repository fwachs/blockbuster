package com.casumo.blockbuster.core;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;

import com.casumo.blockbuster.exception.OutOfStockException;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "films")
@NamedQueries({ @NamedQuery(name = "com.casumo.blockbuster.core.Film.findAll", query = "SELECT f FROM Film f") })
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id", nullable = false)
    private FilmType type;
    private String name;
    private int stock;

    @Version
    private Integer version;

    public Film() {
        // here for hibernate
    }

    public Film(long id, FilmType type, String name, int stock) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.stock = stock;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @JsonProperty
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @JsonProperty
    public FilmType getType() {
        return type;
    }

    public void setType(FilmType type) {
        this.type = type;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void decreaseStock() throws OutOfStockException {
        if (stock == 0)
            throw new OutOfStockException();
        this.stock--;
    }

    public boolean canBeRented() {
        return this.stock > 0;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @JsonProperty
    public Integer getVersion() {
        return version;
    }

    public void increaseStock() {
        this.stock++;
    }
}
