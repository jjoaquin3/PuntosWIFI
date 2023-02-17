package com.example.puntos.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.puntos.model.IPuntoDTO;
import com.example.puntos.model.Punto;
import com.example.puntos.repository.IPuntoRepository;

@Service
public class PuntoService
{
	private final IPuntoRepository puntoRepository;

	public PuntoService(IPuntoRepository puntoRepository)
	{
		this.puntoRepository = puntoRepository;
	}

	public List<Punto> findAll2()
	{
		Pageable paging = PageRequest.of(1, 10);
		Page<Punto> lista = this.puntoRepository.findAll(paging);
		return lista.toList();
	}

	public List<Punto> findAll(Integer pageNo, Integer pageSize)
	{
		Pageable paging = PageRequest.of(pageNo, pageSize);
		Page<Punto> lista = this.puntoRepository.findAll(paging);
		return lista.toList();
	}

	public Punto findById(String id)
	{
		List<Punto> lista = this.puntoRepository.findById(id);
		if (lista.size() > 0)
			return lista.get(0);
		return null;
	}

	public List<Punto> findByColonia(String colonia, Integer pageNo, Integer pageSize)
	{
		Pageable paging = PageRequest.of(pageNo, pageSize);
		Page<Punto> lista = this.puntoRepository.findByColonia(colonia, paging);

		return lista.toList();
	}

	public List<IPuntoDTO> findByLatitudAndLongitud(Double latitud, Double longitud, Integer pageNo, Integer pageSize)
	{
		Pageable paging = PageRequest.of(pageNo, pageSize);
		Page<IPuntoDTO> lista = this.puntoRepository.findByLatitudAndLongitud(latitud, longitud, paging);
		return lista.toList();
	}

	//	public List<Punto> findByLatitudAndLongitud2(Double latitud, Double longitud)
	//	{
	//		return this.puntoRepository.findByLatitudAndLongitud(latitud, longitud);
	//	}
}
