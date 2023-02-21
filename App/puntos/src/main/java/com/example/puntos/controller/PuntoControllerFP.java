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

import com.example.puntos.model.Punto;
import com.example.puntos.service.PuntoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/puntosfp")
@CrossOrigin(origins = "*")
@Tag(
		name = "Puntos WIFI by Programación Funcional",
		description = "Controlador para operaciones a traves de programación funcional sobre puntos WIFI en Ciuda de Mexico")
public class PuntoControllerFP
{
	private final PuntoService puntoService;

	public PuntoControllerFP(PuntoService puntoService)
	{
		this.puntoService = puntoService;
	}

	@Operation(summary = "Obtiene lista paginada de puntos WIFI almacenados en DB")
	@Parameter(name = "page", example = "page", required = false)
	@Parameter(name = "size", example = "size", required = false)
	@GetMapping(produces = "application/json")
	public List<Punto> findAllFP(
			@RequestParam(value = "page", defaultValue = "0", required = false) int page,
			@RequestParam(value = "size", defaultValue = "10", required = false) int size
	)
	{
		return puntoService.findAllFP(page, size);
	}

	@Operation(summary = "Obtiene determinado punto WIFI a traves de parametro")
	@Parameter(name = "id", example = "123", required = true)
	@GetMapping(value = "/{id}", produces = "application/json")
	public Punto findByIdFP(@PathVariable("id") String id)
	{
		try
		{
			return puntoService.findByIdFP(id);
		}
		catch (NoSuchElementException e)
		{
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@Operation(summary = "Obtiene lista paginada de puntos WIFI a traves de Colonia especifica")
	@Parameter(name = "colonia", example = "Nimajuyu", required = true)
	@Parameter(name = "page", example = "page", required = false)
	@Parameter(name = "size", example = "size", required = false)
	@GetMapping(value = "/colonias/{colonia}", produces = "application/json")
	public List<Punto> findByColoniaFP(
			@PathVariable("colonia") String colonia, @RequestParam(value = "page", defaultValue = "0", required = false) int page,
			@RequestParam(value = "size", defaultValue = "10", required = false) int size
	)
	{
		return puntoService.findByColoniaFP(colonia, page, size);
	}

	@Operation(summary = "Obtiene lista paginada de puntos WIFI en orden de menor a mayor distancia")
	@Parameter(name = "latitud", example = "19.4443523", required = true)
	@Parameter(name = "longitud", example = "-99.1307745", required = true)
	@Parameter(name = "page", example = "page", required = false)
	@Parameter(name = "size", example = "size", required = false)
	@GetMapping(value = "/latitudes/{latitud}/longitudes/{longitud}", produces = "application/json")
	public List<Punto> findByLatitudAndLongitudFP(
			@PathVariable("latitud") Double latitud, @PathVariable("longitud") Double longitud,
			@RequestParam(value = "page", defaultValue = "0", required = false) int page,
			@RequestParam(value = "size", defaultValue = "10", required = false) int size
	)
	{
		return puntoService.findByLatitudAndLongitudFP(latitud, longitud, page, size);
	}
}
