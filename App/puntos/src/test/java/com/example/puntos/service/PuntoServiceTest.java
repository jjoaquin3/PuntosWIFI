package com.example.puntos.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

import com.example.puntos.model.IPuntoDTO;
import com.example.puntos.model.Punto;
import com.example.puntos.repository.IPuntoRepository;

class PuntoServiceTest
{
	@Mock
	private IPuntoRepository puntoRepository;

	@InjectMocks
	private PuntoService puntoService;

	private Punto punto;

	@BeforeEach
	public void setUp() throws Exception
	{
		punto = Punto.builder().alcaldia("ALC").colonia("COL").dbid((long) 1234).fecha_instalacion(null).id("EL ID")
				.latitud(1.001).longitud(2.002).programa("Varios").build();
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void findAll()
	{
		List<Punto> lista = new ArrayList<Punto>();
		lista.add(punto);
		Page<Punto> pageLista = new PageImpl<Punto>(lista);
		Pageable pageRequest = PageRequest.of(1, 10);
		when(puntoRepository.findAll(pageRequest)).thenReturn(pageLista);

		// Test
		List<Punto> listaTest = puntoService.findAll(1, 10);
		assertEquals(listaTest.size(), pageLista.getNumberOfElements());
	}

	@Test
	public void findAllFP()
	{
		List<Punto> lista = new ArrayList<Punto>();
		lista.add(punto);
		when(puntoRepository.findAll()).thenReturn(lista);

		// Test
		List<Punto> listaTest = puntoService.findAllFP(1, 10);
		assertThat(StreamSupport.stream(listaTest.spliterator(), false).collect(Collectors.toList()).size())
				.isGreaterThanOrEqualTo(0);
	}

	@Test
	public void findById()
	{
		List<Punto> lista = new ArrayList<Punto>();
		lista.add(punto);
		when(puntoRepository.findById("EL ID")).thenReturn(lista);

		// Test
		Punto puntoTest = puntoService.findById("EL ID");
		assertEquals(puntoTest.getId(), punto.getId());
	}

	@Test
	public void findByIdFP()
	{
		List<Punto> lista = new ArrayList<Punto>();
		lista.add(punto);
		when(puntoRepository.findAll()).thenReturn(lista);

		// Test
		Punto puntoTest = puntoService.findByIdFP("EL ID");
		assertEquals(puntoTest.getId(), punto.getId());
	}

	@Test
	public void findByColonia()
	{
		List<Punto> lista = new ArrayList<Punto>();
		lista.add(punto);

		Page<Punto> pageLista = new PageImpl<Punto>(lista);
		Pageable pageRequest = PageRequest.of(1, 10);
		when(puntoRepository.findByColonia("COL", pageRequest)).thenReturn(pageLista);

		// Test
		List<Punto> listaTest = puntoService.findByColonia("COL", 1, 10);
		assertEquals(listaTest.size(), pageLista.getNumberOfElements());
	}

	@Test
	public void findByColoniaFP()
	{
		List<Punto> lista = new ArrayList<Punto>();
		lista.add(punto);
		when(puntoRepository.findAll()).thenReturn(lista);

		// Test
		Iterable<Punto> listaTest = puntoService.findByColoniaFP("COL", 1, 10);
		assertThat(StreamSupport.stream(listaTest.spliterator(), false).collect(Collectors.toList()).size())
				.isGreaterThanOrEqualTo(0);
	}

	@Test
	public void findByLatitudAndLongitud()
	{
		List<IPuntoDTO> lista = new ArrayList<IPuntoDTO>();
		ProjectionFactory factory = new SpelAwareProxyProjectionFactory();
		IPuntoDTO projection = factory.createProjection(IPuntoDTO.class);
		lista.add(projection);

		Page<IPuntoDTO> pageLista = new PageImpl<IPuntoDTO>(lista);
		Pageable pageRequest = PageRequest.of(1, 10);

		when(puntoRepository.findByLatitudAndLongitud(1.001, 2.002, pageRequest)).thenReturn(pageLista);

		// Test
		List<IPuntoDTO> listaTest = puntoService.findByLatitudAndLongitud(1.001, 2.002, 1, 10);
		assertEquals(listaTest.size(), pageLista.getNumberOfElements());
	}

	@Test
	public void findByLatitudAndLongitudFP()
	{
		List<Punto> lista = new ArrayList<Punto>();
		lista.add(punto);
		when(puntoRepository.findAll()).thenReturn(lista);

		// Test
		List<Punto> listaTest = puntoService.findByLatitudAndLongitudFP(1.001, 2.002, 1, 10);
		assertThat(StreamSupport.stream(listaTest.spliterator(), false).collect(Collectors.toList()).size())
				.isGreaterThanOrEqualTo(0);
	}

}
