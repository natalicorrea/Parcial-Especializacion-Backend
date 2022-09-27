package com.dh.catalogservice.client;

import com.dh.catalogservice.Model.MovieDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "movie-service")
public interface MovieClient {

    @GetMapping("/movies/{genre}")
    public ResponseEntity<List<MovieDto>> getMovieByGenre(@PathVariable(value = "genre") String genre);

    @GetMapping("/movies/withErrors/{genre}")
    public ResponseEntity<List<MovieDto>> getMovieByGenreWithThrowError(@PathVariable(value = "genre") String genre,
                                                                        @RequestParam("throwError") boolean throwError);
}
