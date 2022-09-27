package com.dh.catalogservice.Service;

import com.dh.catalogservice.Model.CatalogDto;
import com.dh.catalogservice.Model.MovieDto;
import com.dh.catalogservice.Model.SerieDto;
import com.dh.catalogservice.client.MovieClient;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CatalogService {

    //Variable de entorno para el nombre de la cola que configuramos
    @Value("${queue.movie.name}")
    private String queueName;

    @Value("${queue.series.name}")
    private String queueSeriesName;

    private final RabbitTemplate rabbitTemplate;

    public final Logger LOG = LoggerFactory.getLogger(CatalogService.class);
    private final MovieClient movieClient;

    @Autowired
    public CatalogService(MovieClient movieClient, RabbitTemplate rabbitTemplate){
        this.movieClient = movieClient;
        this.rabbitTemplate = rabbitTemplate;
    }

    //obtener peliculas por genero
    public ResponseEntity<List<MovieDto>> findMovieByGenre(String genre){
        LOG.info("Buscando peliculas por genero" + genre);
        return movieClient.getMovieByGenre(genre);
    }


    //"movies" -> nombre instancia en circuitBreaker config
    @CircuitBreaker(name = "movies", fallbackMethod = "moviesFallBackMethod")
    public ResponseEntity<List<MovieDto>> findMovieByGenre(String genre, Boolean throwError){
        LOG.info("Buscando peliculas por genero" + genre);
        return movieClient.getMovieByGenreWithThrowError(genre, throwError);
    }

    //metodo de fallback
    public ResponseEntity<List<MovieDto>> moviesFallBackMethod(CallNotPermittedException exception){
        LOG.info("Error al buscar peliculas por genero, CIRCUIT BREAKER ACTIVADO");
        //retorno listra vacia pero podria devolver valores guardados en cache u otra logica
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
    }

    //guardar pelicula con RabbitMQ -
    public void saveMovie(MovieDto movieDto){
        /*recibe pelicula y conecta con rabbitMQ para enviarla a la cola*/
        rabbitTemplate.convertAndSend(queueName, movieDto);
    }

    public void saveSeries(SerieDto serieDto){
        rabbitTemplate.convertAndSend(queueSeriesName, serieDto);
    }















}
