package com.example.puntos.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.puntos.model.Punto;
import com.example.puntos.service.PuntoService;

class PuntoControllerTest
{
	@Mock
	private PuntoService puntoService;

	@InjectMocks
	private PuntoController puntoController;

	private Punto punto;

	@BeforeEach
	public void setUp() throws Exception
	{
		punto = Punto.builder().alcaldia("ALC").colonia("COL").dbid((long) 1234).fecha_instalacion(null).id("EL ID")
				.latitud(1.001).longitud(2.002).programa("Varios").build();
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void FindAll()
	{
		// public ResponseEntity<List<Punto>>
		List<Punto> lista = new ArrayList<Punto>();
		lista.add(punto);
		when(puntoService.findAll(1, 10)).thenReturn(lista);

		// Test
		List<Punto> resultTest = puntoController.findAll(1, 10);
		assertThat(resultTest.size()).isGreaterThanOrEqualTo(1);
	}

	@Test
	public void FindByID()
	{
		when(puntoService.findById("2L")).thenReturn(punto);

		// Test
		Punto resultTest = puntoController.findById("2L");
		assertThat(resultTest.getId().equals("EL ID"));
	}

	@Test
	public void findByColonia()
	{
		List<Punto> lista = new ArrayList<Punto>();
		lista.add(punto);
		when(puntoService.findByColonia("COL", 1, 10)).thenReturn(lista);

		// Test
		List<Punto> resultTest = puntoController.findByColonia("COL", 1, 10);
		assertThat(resultTest.size()).isGreaterThanOrEqualTo(1);
	}

	/*
	@Test
	public void findByLatitudAndLongitud()
	{
		List<Punto> lista = new ArrayList<Punto>();
		lista.add(punto);
		when(puntoService.findByLatitudAndLongitud(1.001, 1.002, 1, 10)).thenReturn(lista);
	
		// Test
		List<Punto> resultTest = puntoController.findByLatitudAndLongitud(1.001, 2.002, 1, 10);
		assertThat(resultTest.size()).isGreaterThanOrEqualTo(0);
	}*/

}
