package com.example.puntos.controller;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.example.puntos.model.IPuntoDTO;
import com.example.puntos.model.Punto;
import com.example.puntos.service.PuntoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@Tag(name = "Puntos WIFI GraphQL", description = "Controlador para operaciones sobre puntos WIFI en Ciuda de Mexico con GraphQL")
public class PuntoControllerGQ
{
	private final PuntoService puntoService;

	public PuntoControllerGQ(PuntoService puntoService)
	{
		this.puntoService = puntoService;
	}

	@Operation(summary = "Obtiene lista paginada de puntos WIFI almacenados en DB")
	@Parameter(name = "page", example = "page", required = false)
	@Parameter(name = "size", example = "size", required = false)
	@QueryMapping
	public List<Punto> puntosFindAll(@Argument int page, @Argument int size)
	{
		return puntoService.findAll(page, size);
	}

	@Operation(summary = "Obtiene determinado punto WIFI a traves de parametro")
	@Parameter(name = "id", example = "123", required = true)
	@QueryMapping
	public Punto puntosFindById(@Argument String id)
	{
		return puntoService.findById(id);
	}

	@Operation(summary = "Obtiene lista paginada de puntos WIFI a traves de Colonia especifica")
	@Parameter(name = "colonia", example = "Nimajuyu", required = true)
	@Parameter(name = "page", example = "page", required = false)
	@Parameter(name = "size", example = "size", required = false)
	@QueryMapping
	public List<Punto> puntosFindByColonia(
			@Argument String colonia, @Argument Integer page, @Argument Integer size
	)
	{
		return puntoService.findByColonia(colonia, page, size);
	}

	@Operation(summary = "Obtiene lista paginada de puntos WIFI en orden de menor a mayor distancia")
	@Parameter(name = "latitud", example = "19.4443523", required = true)
	@Parameter(name = "longitud", example = "-99.1307745", required = true)
	@Parameter(name = "page", example = "page", required = false)
	@Parameter(name = "size", example = "size", required = false)
	@QueryMapping
	public List<IPuntoDTO> puntosFindByLatitudAndLongitud(
			@Argument Float latitud, @Argument Float longitud, @Argument Integer page, @Argument Integer size
	)
	{
		return puntoService.findByLatitudAndLongitud((double) latitud, (double) longitud, page, size);
	}

	@Operation(summary = "Obtiene lista paginada de puntos WIFI almacenados en DB  con programación funcional")
	@Parameter(name = "page", example = "page", required = false)
	@Parameter(name = "size", example = "size", required = false)
	@QueryMapping
	public List<Punto> puntosFindAllFP(@Argument int page, @Argument int size)
	{
		return puntoService.findAllFP(page, size);
	}

	@Operation(summary = "Obtiene determinado punto WIFI a traves de parametro con programación funcional")
	@Parameter(name = "id", example = "123", required = true)
	@QueryMapping
	public Punto puntosFindByIdFP(@Argument String id)
	{
		return puntoService.findByIdFP(id);
	}

	@Operation(summary = "Obtiene lista paginada de puntos WIFI a traves de Colonia especifica  con programación funcional")
	@Parameter(name = "colonia", example = "Nimajuyu", required = true)
	@Parameter(name = "page", example = "page", required = false)
	@Parameter(name = "size", example = "size", required = false)
	@QueryMapping
	public List<Punto> puntosFindByColoniaFP(
			@Argument String colonia, @Argument Integer page, @Argument Integer size
	)
	{
		return puntoService.findByColoniaFP(colonia, page, size);
	}

	@Operation(summary = "Obtiene lista paginada de puntos WIFI en orden de menor a mayor distancia con programación funcional")
	@Parameter(name = "latitud", example = "19.4443523", required = true)
	@Parameter(name = "longitud", example = "-99.1307745", required = true)
	@Parameter(name = "page", example = "page", required = false)
	@Parameter(name = "size", example = "size", required = false)
	@QueryMapping
	public List<Punto> puntosFindByLatitudAndLongitudFP(
			@Argument Float latitud, @Argument Float longitud, @Argument Integer page, @Argument Integer size
	)
	{
		return puntoService.findByLatitudAndLongitudFP((double) latitud, (double) longitud, page, size);
	}
}
