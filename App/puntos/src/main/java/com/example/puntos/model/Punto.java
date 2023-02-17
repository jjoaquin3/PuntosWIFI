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
public class Punto
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long dbid;

	@Column(name = "id", nullable = true, length = 256)
	private String id;

	@Column(name = "programa", nullable = true, length = 256)
	private String programa;

	@Column(name = "fecha_instalacion", nullable = true)
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate fecha_instalacion;

	@Column(name = "latitud", nullable = true, precision = 32, scale = 24)
	private Double latitud;

	@Column(name = "longitud", nullable = true, precision = 32, scale = 24)
	private Double longitud;

	@Column(name = "colonia", nullable = true, length = 256)
	private String colonia;

	@Column(name = "alcaldia", nullable = true, length = 256)
	private String alcaldia;

}
