package com.example.puntos.service;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.puntos.model.IPuntoDTO;
import com.example.puntos.model.Punto;
import com.example.puntos.model.PuntoDistanciaDTO;
import com.example.puntos.repository.IPuntoRepository;

import io.swagger.v3.oas.annotations.Operation;

@Service
public class PuntoService
{
	private final IPuntoRepository puntoRepository;

	public PuntoService(IPuntoRepository puntoRepository)
	{
		this.puntoRepository = puntoRepository;
	}

	@Operation(summary = "Obtiene lista paginada de puntos WIFI almacenados en DB")
	public List<Punto> findAll(Integer pageNo, Integer pageSize)
	{
		// 1. Crear paging para limitar por pagina y tamaño
		// 2. Retornar lista con ayuda de Query Methods

		Pageable paging = PageRequest.of(pageNo, pageSize);
		return this.puntoRepository.findAll(paging).toList();
	}

	@Operation(summary = "Obtiene determinado punto WIFI a traves de parametro")
	public Punto findById(String id)
	{
		// 1. Obtener la lista de posibles coincidencias
		// 2. Retornar lista con ayuda de Query Methods

		List<Punto> lista = this.puntoRepository.findById(id);
		return StreamSupport.stream(lista.spliterator(), false).findFirst().orElse(null);
	}

	@Operation(summary = "Obtiene lista paginada de puntos WIFI a traves de Colonia especifica")
	public List<Punto> findByColonia(String colonia, Integer pageNo, Integer pageSize)
	{
		// 1. Crear paging para limitar por pagina y tamaño
		// 2. Retornar lista con ayuda de Query Methods

		Pageable paging = PageRequest.of(pageNo, pageSize);
		return this.puntoRepository.findByColonia(colonia, paging).toList();
	}

	@Operation(summary = "Obtiene lista paginada de puntos WIFI en orden de menor a mayor distancia")
	public List<IPuntoDTO> findByLatitudAndLongitud(Double latitud, Double longitud, Integer pageNo, Integer pageSize)
	{
		// 1. Crear paging para limitar por pagina y tamaño
		// 2. Retornar lista con ayuda de Query Rewriter

		Pageable paging = PageRequest.of(pageNo, pageSize);
		return this.puntoRepository.findByLatitudAndLongitud(latitud, longitud, paging).toList();
	}

	@Operation(summary = "Obtiene lista paginada de puntos WIFI almacenados en DB con programación funcional")
	public List<Punto> findAllFP(Integer pageNo, Integer pageSize)
	{
		// 1. Obtener la lista de todo la información
		// 2. Retornar lista con uso de streams para limitar por pagina y tamaño

		Iterable<Punto> lista = this.puntoRepository.findAll();
		return StreamSupport.stream(lista.spliterator(), false).skip(pageNo * pageSize).limit(pageSize)
				.collect(Collectors.toList());
	}

	@Operation(summary = "Obtiene determinado punto WIFI a traves de parametro con programación funcional")
	public Punto findByIdFP(String id)
	{
		// 1. Obtener la lista de todo la información
		// 2. Retornar lista con uso de streams para limitar por filtro, pagina y tamaño

		Iterable<Punto> lista = this.puntoRepository.findAll();
		return StreamSupport.stream(lista.spliterator(), false).filter(p -> p.getId().equals(id)).findFirst().orElse(null);
	}

	@Operation(summary = "Obtiene lista paginada de puntos WIFI a traves de Colonia especifica con programación funcional")
	public List<Punto> findByColoniaFP(String colonia, Integer pageNo, Integer pageSize)
	{
		// 1. Obtener la lista de todo la información
		// 2. Retornar lista con uso de streams para limitar por filtro, pagina y tamaño

		Iterable<Punto> lista = this.puntoRepository.findAll();
		return StreamSupport.stream(lista.spliterator(), false).filter(p -> p.getColonia().equals(colonia))
				.skip(pageNo * pageSize).limit(pageSize).collect(Collectors.toList());
	}

	@Operation(summary = "Obtiene lista paginada de puntos WIFI en orden de menor a mayor distancia con programacion funcional")
	public List<Punto> findByLatitudAndLongitudFP(Double latitud, Double longitud, Integer pageNo, Integer pageSize)
	{
		// 0. Obtener la lista de todo la información
		// 1. Función para calcular la distancia entre dos puntos
		// 2. Crear un Stream de las distancias calculadas
		// 3. Ordenar las distancias por orden ascendente
		// 4. Convertir las distancias ordenadas de vuelta a objetos Punto limitando por pagina y tamaño

		Iterable<Punto> puntos = this.puntoRepository.findAll();
		Function<Punto, Double> distancia = punto -> Math
				.sqrt(Math.pow(punto.getLatitud() - latitud, 2) + Math.pow(punto.getLongitud() - longitud, 2));
		Stream<PuntoDistanciaDTO> stream = StreamSupport.stream(puntos.spliterator(), false)
				.map(punto -> new PuntoDistanciaDTO(punto, distancia.apply(punto)));
		stream = stream.sorted(Comparator.comparing(PuntoDistanciaDTO::getDistancia));
		return stream.map(PuntoDistanciaDTO::getPunto).skip(pageNo * pageSize).limit(pageSize).collect(Collectors.toList());

	}

}
