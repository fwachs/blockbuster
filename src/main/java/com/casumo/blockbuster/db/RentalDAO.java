package com.casumo.blockbuster.db;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.casumo.blockbuster.core.Customer;
import com.casumo.blockbuster.core.Rental;
import com.casumo.blockbuster.core.RentedFilm;

import io.dropwizard.hibernate.AbstractDAO;

public class RentalDAO extends AbstractDAO<Rental> {

    public RentalDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void save(Rental rental) {
        persist(rental);
        // I understood that Hibernate should take care of the children
        // if classes were configured correctly, seems I have an error
        // but it's been a while since I last used hibernate.
        // I prefer plain JDBC really.
        for (RentedFilm film : rental.getRentedFilms()) {
            this.currentSession().save(film);
        }
    }

    public List<Rental> findRentalsFor(Customer customer) {
        Criteria criteria = this.currentSession().createCriteria(Rental.class);
        return criteria.add(Restrictions.eq("customer", customer)).list();

    }

    public Rental findBy(long rentalId) {
        return get(rentalId);
    }
}