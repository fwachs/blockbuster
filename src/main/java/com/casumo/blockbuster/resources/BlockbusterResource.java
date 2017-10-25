package com.casumo.blockbuster.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.casumo.blockbuster.api.RentalService;
import com.casumo.blockbuster.client.FilmVO;
import com.casumo.blockbuster.client.RentVO;
import com.casumo.blockbuster.core.Customer;
import com.casumo.blockbuster.core.Film;
import com.casumo.blockbuster.core.Rental;
import com.casumo.blockbuster.core.RentedFilm;

import io.dropwizard.hibernate.UnitOfWork;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class BlockbusterResource {

    private final RentalService rentalService;

    public BlockbusterResource(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GET
    @UnitOfWork
    @Path("/film")
    public List<Film> films(@QueryParam("name") Optional<String> name) {
        return rentalService.findAllFilms();
    }

    @POST
    @UnitOfWork
    @Path("/user/{id}/rental")
    public Response rent(@PathParam("id") long id, RentVO vo) {
        Customer customer = rentalService.findCustomerBy(id);
        if (customer == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        List<RentedFilm> filmsToRent = new ArrayList<RentedFilm>();
        for (FilmVO filmVo : vo.films) {
            Film film = rentalService.findFilmFor(filmVo.filmId);
            if (film != null) {
                RentedFilm rentedFilm = new RentedFilm(film, filmVo.days);
                filmsToRent.add(rentedFilm);
            }
        }
        if (filmsToRent.isEmpty()) {
            return Response.status(Status.NO_CONTENT).build();
        }

        Rental rental = rentalService.rent(customer, filmsToRent);
        return Response.ok(rental).build();
    }

    @POST
    @UnitOfWork
    @Path("/user/{id}/rental/{rentalId}")
    public Response returnFilms(@PathParam("id") long id, @PathParam("rentalId") long rentalId, List<Long> filmIds)
            throws Exception {
        Customer customer = rentalService.findCustomerBy(id);
        if (customer == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        Rental rental = rentalService.findRentalBy(rentalId);
        rental = rentalService.returnFilms(customer, rental, filmIds);
        return Response.ok(rental).build();
    }

    @GET
    @UnitOfWork
    @Path("/user/{id}/points")
    public Response bonusPointsFor(@PathParam("id") long id) throws Exception {
        Customer customer = rentalService.findCustomerBy(id);
        if (customer == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.ok(rentalService.calculateBonusPointsFor(customer)).build();
    }
}
