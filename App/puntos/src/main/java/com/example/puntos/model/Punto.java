package com.example.puntos.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "PUNTO")
@Schema(description = "Modelo que representa la información de los puntos WIFI")
public class Punto
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(required = true, description = "Icremental, Llave Subrogada")
	private Long dbid;

	@Column(name = "id", nullable = true, length = 256)
	@Schema(required = false, description = "ID proveniente de la data raw")
	private String id;

	@Column(name = "programa", nullable = true, length = 256)
	@Schema(required = false,  description = "Programa social sobre el cual se genero el servicio de instalacion")
	private String programa;

	@Column(name = "fecha_instalacion", nullable = true)
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(pattern = "dd/MM/yyyy")
	@Schema(required = false,  description = "Fecha de instalacion")
	private LocalDate fecha_instalacion;

	@Column(name = "latitud", nullable = true, precision = 32, scale = 24)
	@Schema(required = false,  description = "Proporciona la localización de un lugar, corren de este-oeste y marcan la posicion norte-sur desde el ecuador")
	private Double latitud;
	
	@Schema(required = false,  description = "Proporciona la localización de un lugar, corren de norte a sur y marcan la posicion este-oeste de un meridiano")	
	@Column(name = "longitud", nullable = true, precision = 32, scale = 24)
	private Double longitud;

	@Schema(required = false,  description = "Colonia (lugar) en donde se encuentra el punto wifi")
	@Column(name = "colonia", nullable = true, length = 256)
	private String colonia;

	@Schema(required = false,  description = "Alcaldia responsable del punto wifi")
	@Column(name = "alcaldia", nullable = true, length = 256)
	private String alcaldia;

}
