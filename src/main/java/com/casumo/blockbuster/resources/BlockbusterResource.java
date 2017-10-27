package com.casumo.blockbuster.resources;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.casumo.blockbuster.exception.AlreadyReturnedException;
import com.casumo.blockbuster.exception.OutOfStockException;
import com.fasterxml.jackson.annotation.JsonProperty;

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

        // TODO: implement query with in (ids) to avoid querying for each movie
        List<RentedFilm> filmsToRent = new ArrayList<RentedFilm>();
        for (FilmVO filmVo : vo.films) {
            Film film = rentalService.findFilmBy(filmVo.filmId);
            if (film != null) {
                RentedFilm rentedFilm = new RentedFilm(film, filmVo.days);
                filmsToRent.add(rentedFilm);
            }
        }
        if (filmsToRent.isEmpty()) {
            return Response.status(Status.NO_CONTENT).entity(new Message("response", "No movies found for given ids"))
                    .build();
        }

        Response response;
        try {
            Rental rental = rentalService.rent(customer, filmsToRent);
            response = Response.ok(rental).build();
        } catch (OutOfStockException e) {
            // there was no stock for any of the movies that wanted to be rented
            response = Response.status(Status.NO_CONTENT)
                    .entity(new Message("response", "No stock available for any movie")).build();
        }
        return response;
    }

    @POST
    @UnitOfWork
    @Path("/user/{id}/rental/{rentalId}")
    public Response returnFilms(@PathParam("id") long id, @PathParam("rentalId") long rentalId, List<Long> filmIds) {
        Customer customer = rentalService.findCustomerBy(id);
        Rental rental = rentalService.findRentalBy(rentalId);

        if (customer == null) {
            return Response.status(Status.NOT_FOUND).entity(error("No user found for id: " + id)).build();
        }
        if (rental == null) {
            return Response.status(Status.NOT_FOUND).entity(error("No rental found for id: " + rentalId)).build();
        }

        Response response;
        try {
            rental = rentalService.returnFilms(customer, rental, filmIds, new Date());
            response = Response.ok(rental).build();
        } catch (AlreadyReturnedException e) {
            response = Response.status(Status.BAD_REQUEST).entity(error(e.getMessage())).build();
        }
        return response;
    }

    @GET
    @UnitOfWork
    @Path("/user/{id}/points")
    public Response bonusPointsFor(@PathParam("id") long id) {
        Customer customer = rentalService.findCustomerBy(id);
        if (customer == null) {
            return Response.status(Status.NOT_FOUND).entity(error("No user found for id: " + id)).build();
        }

        Integer points = rentalService.calculateBonusPointsFor(customer);
        return Response.ok(new Message("points", points)).build();
    }

    private Message error(String msg) {
        return new Message("error", msg);
    }

    class Message {
        @JsonProperty
        private Map<String, Object> response;

        public Message(String key, Object obj) {
            this.response = new HashMap<String, Object>();
            this.response.put(key, obj);
        }
    }
}
