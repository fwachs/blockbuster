package com.casumo.blockbuster.api;

import java.util.Date;
import java.util.List;

import com.casumo.blockbuster.core.Customer;
import com.casumo.blockbuster.core.Film;
import com.casumo.blockbuster.core.Rental;
import com.casumo.blockbuster.core.RentedFilm;
import com.casumo.blockbuster.exception.AlreadyReturnedException;
import com.casumo.blockbuster.exception.OutOfStockException;

public interface RentalService {

    List<Film> findAllFilms();

    Rental rent(Customer customer, List<RentedFilm> films) throws OutOfStockException;

    Rental returnFilms(Customer customer, Rental rental, List<Long> filmIds, Date returnedOn) throws AlreadyReturnedException;

    Film findFilmBy(long filmId);

    int calculateBonusPointsFor(Customer customer);

    Customer findCustomerBy(long customerId);

    Rental findRentalBy(long rentalId);

}
