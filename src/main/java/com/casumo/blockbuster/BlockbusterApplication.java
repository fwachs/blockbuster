package com.casumo.blockbuster;

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
import com.casumo.blockbuster.resources.BlockbusterResource;

import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class BlockbusterApplication extends Application<BlockbusterConfiguration> {

    public static void main(final String[] args) throws Exception {
        new BlockbusterApplication().run(args);
    }

    @Override
    public String getName() {
        return "Blockbuster";
    }

    private final HibernateBundle<BlockbusterConfiguration> hibernateBundle = new HibernateBundle<BlockbusterConfiguration>(
            Customer.class, Film.class, FilmType.class, Price.class, Rental.class, RentedFilm.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(BlockbusterConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    @Override
    public void initialize(final Bootstrap<BlockbusterConfiguration> bootstrap) {
        bootstrap.setConfigurationSourceProvider(new SubstitutingSourceProvider(
                bootstrap.getConfigurationSourceProvider(), new EnvironmentVariableSubstitutor(false)));

        bootstrap.addBundle(new MigrationsBundle<BlockbusterConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(BlockbusterConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
        bootstrap.addBundle(hibernateBundle);
    }

    @Override
    public void run(final BlockbusterConfiguration configuration, final Environment environment) {
        final CustomerDAO customerDAO = new CustomerDAO(hibernateBundle.getSessionFactory());
        final FilmDAO filmDAO = new FilmDAO(hibernateBundle.getSessionFactory());
        final RentalDAO rentalDAO = new RentalDAO(hibernateBundle.getSessionFactory());
        final RentalService rentalService = new RentalServiceImpl(rentalDAO, filmDAO, customerDAO);

        environment.jersey().register(new BlockbusterResource(rentalService));
    }

}
