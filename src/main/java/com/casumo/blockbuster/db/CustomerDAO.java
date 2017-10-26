package com.casumo.blockbuster.db;

import java.util.Optional;

import org.hibernate.SessionFactory;

import com.casumo.blockbuster.core.Customer;

import io.dropwizard.hibernate.AbstractDAO;

public class CustomerDAO extends AbstractDAO<Customer> {

    public CustomerDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<Customer> findBy(Long id) {
        return Optional.ofNullable(get(id));
    }

}
