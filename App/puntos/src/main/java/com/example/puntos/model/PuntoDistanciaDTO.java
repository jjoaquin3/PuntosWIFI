package com.example.puntos.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Schema(description = "Pojo para Modelo que representa la información de los puntos WIFI")
public class PuntoDistanciaDTO
{
	@Schema(required = true, description = "Icremental, Llave Subrogada")
	private Punto punto;

	@Schema(
			required = false,
			description = "Proporciona la localización de un lugar, corren de norte a sur y marcan la posicion este-oeste de un meridiano")
	private Double distancia;

}