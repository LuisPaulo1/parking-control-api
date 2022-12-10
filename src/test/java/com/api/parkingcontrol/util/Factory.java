package com.api.parkingcontrol.util;

import java.time.LocalDateTime;
import java.util.UUID;

import com.api.parkingcontrol.controllers.dtos.ParkingSpotInputDto;
import com.api.parkingcontrol.controllers.dtos.ParkingSpotResultDto;
import com.api.parkingcontrol.models.ParkingSpotModel;

public class Factory {

	public static ParkingSpotModel createParkingSpotModel(){
		UUID uuid = UUID.fromString("932eda96-638b-4cad-af31-3a4f91fc3a5e");
		return new ParkingSpotModel(uuid,"44AD", "ABC8123", "Fiat", "cvb", "preto", LocalDateTime.now(), LocalDateTime.now(), "Carlos", "300", "G");
	}

	public static ParkingSpotInputDto createParkingSpotInputDto(){
		return new ParkingSpotInputDto("44AD", "ABC8123", "Fiat", "cvb", "preto", "Carlos", "300", "G");
	}

	public static ParkingSpotResultDto createParkingSpotResultDto(){
		UUID uuid = UUID.fromString("932eda96-638b-4cad-af31-3a4f91fc3a5e");
		return new ParkingSpotResultDto(uuid,"44AD", "ABC8123", "Fiat", "cvb", "preto", LocalDateTime.now(), LocalDateTime.now(), "Carlos", "300", "G");
	}

}
