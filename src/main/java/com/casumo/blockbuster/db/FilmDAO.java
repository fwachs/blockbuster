package com.casumo.blockbuster.db;

import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;

import com.casumo.blockbuster.core.Film;

import io.dropwizard.hibernate.AbstractDAO;

public class FilmDAO extends AbstractDAO<Film> {

    public FilmDAO(SessionFactory factory) {
        super(factory);
    }

    public List<Film> findAll() {
        return list(namedQuery("com.casumo.blockbuster.core.Film.findAll"));
    }

    public Optional<Film> findById(Long id) {
        return Optional.ofNullable(get(id));
    }

}
