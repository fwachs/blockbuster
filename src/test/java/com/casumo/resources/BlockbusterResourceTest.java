package com.casumo.resources;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.casumo.blockbuster.api.RentalService;
import com.casumo.blockbuster.api.RentalServiceImpl;
import com.casumo.blockbuster.core.Customer;
import com.casumo.blockbuster.core.Film;
import com.casumo.blockbuster.core.FilmType;
import com.casumo.blockbuster.core.Price;
import com.casumo.blockbuster.core.Rental;
import com.casumo.blockbuster.core.RentedFilm;
import com.casumo.blockbuster.db.CustomerDAO;
import com.casumo.blockbuster.db.FilmDAO;
import com.casumo.blockbuster.db.RentalDAO;

public class BlockbusterResourceTest {
    private RentalService rentalService;
    private static final FilmDAO filmDAO = mock(FilmDAO.class);
    private static final RentalDAO rentalDAO = mock(RentalDAO.class);
    private static final CustomerDAO customerDAO = mock(CustomerDAO.class);

    private List<Film> films;
    private Customer customer;
    private Film matrix;
    private List<RentedFilm> rentedFilms;
    private Rental rental;
    private Film spiderMan;
    private Film spiderMan2;
    private Film outOfAfrica;

    @Before
    public void setup() {
        rentalService = new RentalServiceImpl(rentalDAO, filmDAO, customerDAO);
        customer = new Customer(1L, "Federico Wachs");
        setupFilms();
        setupRentedFilms();
        setupRental();
    }

    private void setupRentedFilms() {
        rentedFilms = new ArrayList<RentedFilm>();
        rentedFilms.add(new RentedFilm(matrix, 1));
        rentedFilms.add(new RentedFilm(spiderMan, 5));
        rentedFilms.add(new RentedFilm(spiderMan2, 2));
        rentedFilms.add(new RentedFilm(outOfAfrica, 7));
    }

    private void setupRental() {
        rental = new Rental(customer, rentedFilms);
    }

    private void setupFilms() {
        films = new ArrayList<Film>();
        Price basic = new Price(1L, 30, "BASIC");
        Price premium = new Price(1L, 40, "PREMIUM");

        FilmType newRelease = new FilmType(1L, premium, "NEW", 2, 0);
        FilmType regular = new FilmType(2L, basic, "NEW", 1, 3);
        FilmType old = new FilmType(3L, basic, "NEW", 1, 5);

        matrix = new Film(1L, newRelease, "Matrix 11", 10);
        spiderMan = new Film(2L, regular, "Spider Man", 10);
        spiderMan2 = new Film(3L, regular, "Spider Man 2 ", 10);
        outOfAfrica = new Film(4L, old, "Out Of Africa", 10);

        films.add(matrix);
        films.add(spiderMan);
        films.add(spiderMan2);
        films.add(outOfAfrica);
    }

    @After
    public void tearDown() {
        reset(rentalDAO);
        reset(filmDAO);
        reset(customerDAO);
    }

    @Test
    public void testFindAllFilms() {
        when(filmDAO.findAll()).thenReturn(films);

        List<Film> films = rentalService.findAllFilms();

        assertThat(films.size()).isEqualTo(this.films.size());
        assertThat(films).isEqualTo(this.films);
        verify(filmDAO).findAll();
    }

    @Test
    public void testFindCustomer() {
//        when(customerDAO.findById(1L)).thenReturn();
    }
//    @Test
//    public void testRent() {
//        when(rentalService.findCustomerBy(1L)).thenReturn(customer);
//        when(rentalService.rent(customer, rentedFilms)).thenReturn(rental);
//
//        Customer customer = rentalService.findCustomerBy(1L);
//        Rental rental = rentalService.rent(customer, rentedFilms);
//
//        assertThat(rental.calculateInitialPrice()).isEqualTo(250);
//        assertThat(rental.calculateInitialPrice()).isEqualTo(this.rental.calculateInitialPrice());
//        verify(rentalService).findCustomerBy(1L);
//        verify(rentalService).rent(customer, rentedFilms);
//    }
//
//    @Test
//    public void testReturn() {
//        when(rentalService.findCustomerBy(1L)).thenReturn(customer);
//        when(rentalService.rent(customer, rentedFilms)).thenReturn(rental);
//
//        Customer customer = rentalService.findCustomerBy(1L);
//        Rental rental = rentalService.rent(customer, rentedFilms);
//
//        assertThat(rental.calculateInitialPrice()).isEqualTo(250);
//        assertThat(rental.calculateInitialPrice()).isEqualTo(this.rental.calculateInitialPrice());
//        verify(rentalService).findCustomerBy(1L);
//        verify(rentalService).rent(customer, rentedFilms);
//    }
}