package com.api.parkingcontrol.controllers;

import java.util.UUID;
import javax.validation.Valid;

import com.api.parkingcontrol.controllers.dtos.ParkingSpotInputDto;
import com.api.parkingcontrol.controllers.dtos.ParkingSpotResultDto;
import com.api.parkingcontrol.services.ParkingSpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/parking-spot")
public class ParkingSpotController {

	@Autowired
	private ParkingSpotService parkingSpotService;

	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	@GetMapping
	public ResponseEntity<Page<ParkingSpotResultDto>> getAllParkingSpots(
		@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
		return ResponseEntity.ok(parkingSpotService.findAll(pageable));
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	@GetMapping("/{id}")
	public ResponseEntity<ParkingSpotResultDto> getOneParkingSpot(@PathVariable(value = "id") UUID id) {
		ParkingSpotResultDto parkingSpotResultDto = parkingSpotService.findById(id);
		return ResponseEntity.ok(parkingSpotResultDto);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping
	public ResponseEntity<ParkingSpotResultDto> saveParkingSpot(@RequestBody @Valid ParkingSpotInputDto parkingSpotInputDto) {
		ParkingSpotResultDto parkingSpotResultDto = parkingSpotService.save(parkingSpotInputDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotResultDto);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<ParkingSpotResultDto> updateParkingSpot(@PathVariable(value = "id") UUID id,
		@RequestBody @Valid ParkingSpotInputDto parkingSpotInputDto) {
		ParkingSpotResultDto parkingSpotResultDto = parkingSpotService.update(id, parkingSpotInputDto);
		return ResponseEntity.status(HttpStatus.OK).body(parkingSpotResultDto);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteParkingSpot(@PathVariable(value = "id") UUID id) {
		parkingSpotService.delete(id);
		return ResponseEntity.status(HttpStatus.OK).body("Ponto de estacionamento excluído com sucesso.");
	}
}