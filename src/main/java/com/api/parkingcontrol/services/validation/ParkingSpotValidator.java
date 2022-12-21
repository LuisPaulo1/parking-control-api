package com.api.parkingcontrol.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.api.parkingcontrol.controllers.dtos.ParkingSpotInputDto;
import com.api.parkingcontrol.controllers.exception.FieldMessage;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.repositories.ParkingSpotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

public class ParkingSpotValidator implements ConstraintValidator<ParkingSpotValid, ParkingSpotInputDto> {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private ParkingSpotRepository parkingSpotRepository;

	@Override public void initialize(ParkingSpotValid constraintAnnotation) {
	}

	@Override public boolean isValid(ParkingSpotInputDto dto, ConstraintValidatorContext context) {

		boolean lincesePlateCar = parkingSpotRepository.existsByLicensePlateCar(dto.getLicensePlateCar());
		boolean parkingSpotNumber = parkingSpotRepository.existsByParkingSpotNumber(dto.getParkingSpotNumber());
		boolean apartmentAndBlock = parkingSpotRepository.existsByApartmentAndBlock(dto.getApartment(), dto.getBlock());

		List<FieldMessage> list = new ArrayList<>();

		if (request.getMethod().equals("PUT")) {
			var uriVars = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
			UUID parkingSpotIdUri = UUID.fromString(uriVars.get("id"));
			Optional<ParkingSpotModel> parkingSpotModel = parkingSpotRepository.findById(parkingSpotIdUri);
			if (parkingSpotModel.isPresent()) {
				if (!parkingSpotModel.get().getLicensePlateCar().equals(dto.getLicensePlateCar()) && lincesePlateCar) {
					list.add(new FieldMessage("licensePlateCar", "A licença da placa do carro já está em uso!"));
				}
				if (!parkingSpotModel.get().getParkingSpotNumber().equals(dto.getParkingSpotNumber()) && parkingSpotNumber) {
					list.add(new FieldMessage("parkingSpotNumber", "A vaga de estacionamento já está em uso!"));
				}
				if ((!parkingSpotModel.get().getApartment().equals(dto.getApartment()) || !parkingSpotModel.get().getBlock().equals(dto.getBlock())) && apartmentAndBlock) {
					list.add(new FieldMessage("parkingSpotNumber", "Já existe uma vaga de estacionamento cadastrada para este apartamento/bloco!"));
				}
			}
		} else {
			if (lincesePlateCar) {
				list.add(new FieldMessage("licensePlateCar", "A licença da placa do carro já está em uso!"));
			}
			if (parkingSpotNumber) {
				list.add(new FieldMessage("parkingSpotNumber", "A vaga de estacionamento já está em uso!"));
			}
			if (apartmentAndBlock) {
				list.add(new FieldMessage("parkingSpotNumber", "Já existe uma vaga de estacionamento cadastrada para este apartamento/bloco!"));
			}
		}

		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
				.addConstraintViolation();
		}

		return list.isEmpty();
	}
}
