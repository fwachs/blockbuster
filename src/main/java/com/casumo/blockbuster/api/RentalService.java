package com.casumo.blockbuster.api;

import java.util.List;

import com.casumo.blockbuster.core.Customer;
import com.casumo.blockbuster.core.Film;
import com.casumo.blockbuster.core.Rental;
import com.casumo.blockbuster.core.RentedFilm;
import com.casumo.blockbuster.exception.OutOfStockException;

public interface RentalService {

    List<Film> findAllFilms();

    Rental rent(Customer customer, List<RentedFilm> films) throws OutOfStockException;

    Film findFilmFor(long filmId);

    int calculateBonusPointsFor(Customer customer);

    Customer findCustomerBy(long customerId);

    Rental returnFilms(Customer customer, Rental rental, List<Long> filmIds);

    Rental findRentalBy(long rentalId);

}
