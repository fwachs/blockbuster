package com.casumo.blockbuster.api;

import java.util.ArrayList;
import java.util.List;

import com.casumo.blockbuster.core.Customer;
import com.casumo.blockbuster.core.Film;
import com.casumo.blockbuster.core.Rental;
import com.casumo.blockbuster.core.RentedFilm;
import com.casumo.blockbuster.db.CustomerDAO;
import com.casumo.blockbuster.db.FilmDAO;
import com.casumo.blockbuster.db.RentalDAO;

public class RentalServiceImpl implements RentalService {

    private final FilmDAO filmDAO;
    private final RentalDAO rentalDAO;
    private final CustomerDAO customerDAO;

    public RentalServiceImpl(RentalDAO rentalDAO, FilmDAO filmDAO, CustomerDAO customerDAO) {
        this.rentalDAO = rentalDAO;
        this.filmDAO = filmDAO;
        this.customerDAO = customerDAO;
    }

    public Rental rent(Customer customer, List<RentedFilm> filmsToRent) {
        Rental rental = new Rental(customer);
        List<RentedFilm> films = new ArrayList<RentedFilm>();

        for (RentedFilm filmToRent : filmsToRent) {
            filmToRent.setRental(rental);
            Film film = filmToRent.getFilm();
            if (film.canBeRented()) {
                films.add(filmToRent);
                // should check for optimistic or pssimistic lock. didn't have enough time to figure this out
                film.decreaseStock();
            }
        }

        rental.setRentedFilms(films);
        rentalDAO.save(rental);
        return rental;
    }

    public int calculateBonusPointsFor(Customer customer) {
        int bonusPoints = 0;
        List<Rental> rentals = rentalDAO.findRentalsFor(customer);
        for (Rental rental : rentals) {
            for (RentedFilm rentedFilm : rental.getRentedFilms()) {
                bonusPoints += rentedFilm.getFilm().getType().getBonusPoints();
            }
        }
        return bonusPoints;
    }

    @Override
    public List<Film> findAllFilms() {
        return filmDAO.findAll();
    }

    @Override
    public Film findFilmFor(long filmId) {
        return filmDAO.findById(filmId).orElse(null);
    }

    @Override
    public Customer findCustomerBy(long customerId) {
        return customerDAO.findById(customerId).orElse(null);
    }

    @Override
    public Rental returnFilms(Customer customer, Rental rental, List<Long> films) {
        for (RentedFilm film : rental.getRentedFilms()) {
            if (films.contains(film.getId())) {
                film.markAsReturned();
            }
        }
        rentalDAO.save(rental);
        return rental;
    }

    @Override
    public Rental findRentalBy(long rentalId) {
        return rentalDAO.findBy(rentalId);
    }
}
