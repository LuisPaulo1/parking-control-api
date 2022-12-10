package com.api.parkingcontrol.models;

import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ParkingSpotModelTests {

	@Test
	public void parkingSpotModelDeveriaTerUmaEstruturaCorreta() {

		ParkingSpotModel parkingSpotModel = new ParkingSpotModel();
		parkingSpotModel.setId(UUID.fromString("932eda96-638b-4cad-af31-3a4f91fc3a5e"));
		parkingSpotModel.setParkingSpotNumber("44AD");
		parkingSpotModel.setLicensePlateCar("ABC8123");
		parkingSpotModel.setBrandCar("BMW");
		parkingSpotModel.setModelCar("XXX");
		parkingSpotModel.setColorCar("preto");
		parkingSpotModel.setResponsibleName("Carlos");
		parkingSpotModel.setApartment("200");
		parkingSpotModel.setBlock("E");

		Assertions.assertNotNull(parkingSpotModel.getId());
		Assertions.assertNotNull(parkingSpotModel.getParkingSpotNumber());
		Assertions.assertNotNull(parkingSpotModel.getLicensePlateCar());
		Assertions.assertNotNull(parkingSpotModel.getBrandCar());
		Assertions.assertNotNull(parkingSpotModel.getModelCar());
		Assertions.assertNotNull(parkingSpotModel.getColorCar());
		Assertions.assertNotNull(parkingSpotModel.getResponsibleName());
		Assertions.assertNotNull(parkingSpotModel.getApartment());
		Assertions.assertNotNull(parkingSpotModel.getBlock());
	}

}
