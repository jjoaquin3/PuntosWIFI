package com.example.puntos.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Proyeccion de Modelo que representa la información de los puntos WIFI, tiene mas datos")
public interface IPuntoDTO
{
	@Schema(description = "Distancia entre el punto X, Y y el punto XX(latitud), YY(longitud) dados")
	BigDecimal getDistancia();

	@Schema(required = true, description = "Icremental, Llave Subrogada")
	Long getDbid();

	@Schema(required = false, description = "ID proveniente de la data raw")
	String getId();

	@Schema(description = "Programa social sobre el cual se genero el servicio de instalación")
	String getPrograma();

	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(pattern = "dd/MM/yyyy")
	@Schema(required = false, description = "Fecha de instalacion")
	LocalDate getFecha_instalacion();

	@Schema(
			description = "Proporciona la localización de un lugar, corren de este-oeste y marcan la posicion norte-sur desde el ecuador")
	Double getLatitud();

	@Schema(
			description = "Proporciona la localización de un lugar, corren de norte a sur y marcan la posicion este-oeste de un meridiano")
	Double getLongitud();

	@Schema(description = "Colonia (lugar) en donde se encuentra el punto wifi")
	String getColonia();

	@Schema(description = "Alcaldia responsable del punto wifi")
	String getAlcaldia();
}
