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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@Repository
public interface IPuntoRepository extends PagingAndSortingRepository<Punto, Long>
{
	@Operation(summary = "Obtener todos los Puntos WIFI")
	@Parameter(description = "Pagina sobre la cual se vaciará la información creada en la capa de servicio", name = "pageable", required = true)
	Page<Punto> findAll(Pageable pageable);	
	
	@Operation(summary = "Obtener todos los Puntos WIFI de una Colonia especifica")
	@Parameter(name = "id", description = "\"id\" que sirve de filtro",required = true)
	@Parameter(name = "pageable", description = "Pagina sobre la cual se vaciará la información creada en la capa de servicio", required = true)
	List<Punto> findById(String id);

	@Operation(summary = "Obtener todos los Puntos WIFI de una Colonia especifica")
	@Parameter(name = "colonia", description = "Colonia que sirve de filtro",required = true)
	@Parameter(name = "pageable", description = "Pagina sobre la cual se vaciará la información creada en la capa de servicio", required = true)
	Page<Punto> findByColonia(String colonia, Pageable pageable);

	@Operation(summary = "Obtener todos los Puntos WIFI ordenado de menor a mayor distancia dada la latitud y longitud")
	@Parameter(name = "latitud", description = "Latitud filtro X",required = true)
	@Parameter(name = "longitud", description = "Longitud filtro Y",required = true)
	@Parameter(description = "Pagina sobre la cual se vaciará la información creada en la capa de servicio", name = "pageable", required = true)
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
			+ "	pto.latitud - :latitud  x "
			+ "	,pto.longitud - :longitud y	 "
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
