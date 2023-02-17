package com.example.puntos.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

public interface IPuntoDTO
{
	BigDecimal getDistancia();
	
	Long getDbid();

	String getId();

	String getPrograma();

	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(pattern = "dd/MM/yyyy")
	LocalDate getFecha_instalacion();

	Double getLatitud();

	Double getLongitud();

	String getColonia();

	String getAlcaldia();
}
