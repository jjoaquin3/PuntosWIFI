package com.example.puntos.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.puntos.model.IPuntoDTO;
import com.example.puntos.model.Punto;
import com.example.puntos.service.PuntoService;

@RestController
@RequestMapping("/puntos")
@CrossOrigin(origins = "*")
public class PuntoController
{
	private final PuntoService puntoService;

	public PuntoController(PuntoService puntoService)
	{
		this.puntoService = puntoService;
	}

	@GetMapping(produces = "application/json")
	public List<Punto> findAll(
			@RequestParam(value = "page", defaultValue = "0", required = false) int page,
			@RequestParam(value = "size", defaultValue = "10", required = false) int size
	)
	{
		return puntoService.findAll(page, size);
	}

	@GetMapping(value = "/{id}", produces = "application/json")
	public Punto findById(@PathVariable("id") String id)
	{
		try
		{
			return puntoService.findById(id);
		}
		catch (NoSuchElementException e)
		{
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping(value = "/colonias/{colonia}", produces = "application/json")
	public List<Punto> findByColonia(
			@PathVariable("colonia") String colonia, @RequestParam(value = "page", defaultValue = "0", required = false) int page,
			@RequestParam(value = "size", defaultValue = "10", required = false) int size
	)
	{
		return puntoService.findByColonia(colonia, page, size);
	}

	@GetMapping(value = "/latitudes/{latitud}/longitudes/{longitud}", produces = "application/json")
	public List<IPuntoDTO> findByLatitudAndLongitud(
			@PathVariable("latitud") Double latitud, @PathVariable("longitud") Double longitud,
			@RequestParam(value = "page", defaultValue = "0", required = false) int page,
			@RequestParam(value = "size", defaultValue = "10", required = false) int size
	)
	{
		return puntoService.findByLatitudAndLongitud(latitud, longitud, page, size);
	}
}
