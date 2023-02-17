package com.example.puntos.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.puntos.model.IPuntoDTO;
import com.example.puntos.model.Punto;

@Repository
public interface IPuntoRepository extends PagingAndSortingRepository<Punto, Long>
{
	Page<Punto> findAll(Pageable pageable);
	
	List<Punto> findById(String id);

	Page<Punto> findByColonia(String colonia, Pageable pageable);

	//	Page<Punto> findByLatitudAndLongitud(Double latitud, Double longitud, Pageable pageable);

	@Query(value = "SELECT  "
			+ "	SQRT(sub.x*sub.x + sub.y*sub.y) distancia "
			+ "	,sub.dbid "
			+ "	,sub.alcaldia "
			+ "	,sub.colonia "
			+ "	,sub.fecha_instalacion "
			+ "	,sub.id "
			+ "	,sub.latitud "
			+ "	,sub.longitud "
			+ "	,sub.programa "
			+ "FROM "
			+ "( "
			+ "	SELECT  "
			+ "	:latitud - pto.latitud  x "
			+ "	,:longitud - pto.longitud  y	 "
			+ "	,pto.dbid "
			+ "	,pto.alcaldia "
			+ "	,pto.colonia "
			+ "	,pto.fecha_instalacion "
			+ "	,pto.id "
			+ "	,pto.latitud "
			+ "	,pto.longitud "
			+ "	,pto.programa "
			+ "	FROM PUNTO pto "
			+ ")sub "
			+ "order by distancia asc 	", countQuery="SELECT COUNT(*) FROM PUNTO pto ", nativeQuery = true)
	Page<IPuntoDTO> findByLatitudAndLongitud(@Param("latitud") Double latitud, @Param("longitud") Double longitud, Pageable pageable);

}
