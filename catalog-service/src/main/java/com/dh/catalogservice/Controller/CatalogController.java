package com.dh.catalogservice.Controller;

import com.dh.catalogservice.Model.MovieDto;
import com.dh.catalogservice.Model.SerieDto;
import com.dh.catalogservice.Service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/catalogs")
public class CatalogController {

	private final CatalogService catalogService;

	@Autowired
	public CatalogController(CatalogService catalogService){
		this.catalogService = catalogService;
	}

	// por medio de cliente feign obtengo la lista de peliculas por genero
	@GetMapping("/{genre}")
	public ResponseEntity<List<MovieDto>> getGenre(@PathVariable String genre){
		return catalogService.findMovieByGenre(genre);
	}

	@GetMapping("/withErrors/{genre}")
	public ResponseEntity<List<MovieDto>> getGenre(@PathVariable String genre, @RequestParam("throwError") boolean throwError){
		return catalogService.findMovieByGenre(genre, throwError);
	}

	// Guardar catalogo usando RabbitMQ
	@PostMapping("/save")
	public ResponseEntity<String> saveMovie(@RequestBody MovieDto movieDto){
		catalogService.saveMovie(movieDto);
		return ResponseEntity.ok("Movie was sent to queue");
	}

	//Guardar serie usando RabbitMQ
	@PostMapping("/save/series")
	public ResponseEntity<String> saveSeries(@RequestBody SerieDto serieDto){
		catalogService.saveSeries(serieDto);
		return ResponseEntity.ok("Series was sent to queue");
	}


}
