package com.api.parkingcontrol.services;

import java.util.UUID;

import com.api.parkingcontrol.controllers.dtos.ParkingSpotInputDto;
import com.api.parkingcontrol.controllers.dtos.ParkingSpotResultDto;
import com.api.parkingcontrol.repositories.ParkingSpotRepository;
import com.api.parkingcontrol.services.exception.EntidadeNaoEncontradaException;
import com.api.parkingcontrol.util.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ParkingSpotServiceIT {

	@Autowired
	private ParkingSpotService service;

	@Autowired
	private ParkingSpotRepository repository;

	private ParkingSpotInputDto parkingSpotInputDto;

	private UUID uuid;

	private UUID uuidIncorrect;

	private Pageable pageable;

	@BeforeEach
	void setUp() throws Exception {
		uuid = UUID.fromString("932eda96-638b-4cad-af31-3a4f91fc3a5e");
		uuidIncorrect = UUID.fromString("932eda96-638b-4cad-af31-3a4f91fc3a20");
		parkingSpotInputDto = Factory.createParkingSpotInputDto();
		pageable = PageRequest.of(0, 12);
	}

	@Test
	public void findAllDeveriaRetornarRecursos() {
		Page<ParkingSpotResultDto> result = service.findAll(pageable);
		Assertions.assertFalse(result.isEmpty());
	}

	@Test
	public void findByIdDeveriaRetornarParkingSpotResultDtoQuandoIdExistir() {
		ParkingSpotResultDto parkingSpotResultDto = service.findById(uuid);
		Assertions.assertNotNull(parkingSpotResultDto);
	}

	@Test
	public void findByIdDeveriaLancarEntidadeNaoEncontradaExceptionQuandoIdNaoExistir() {
		Assertions.assertThrows(EntidadeNaoEncontradaException.class, () -> {
			service.findById(uuidIncorrect);
		});
	}

	@Test
	public void saveDeveriaSalvarUmRecurso(){
		 ParkingSpotResultDto parkingSpotResultDto = service.save(parkingSpotInputDto);
		 Assertions.assertNotNull(parkingSpotResultDto);
	}

	@Test
	public void updateDeveriaRetornarAtualizarQuandoIdExistir() {
		ParkingSpotResultDto parkingSpotResultDto = service.update(uuid, parkingSpotInputDto);
		Assertions.assertNotNull(parkingSpotResultDto);
	}

	@Test
	public void updateDeveriaLancarEntidadeNaoEncontradaExceptionQuandoIdNaoExistir() {
		Assertions.assertThrows(EntidadeNaoEncontradaException.class, () -> {
			service.update(uuidIncorrect, parkingSpotInputDto);
		});
	}

	@Test
	public void deleteDeveriaExcluirRecursoQuandoIdExistir() {
		long totalParkingSport = repository.count();
		service.delete(uuid);
		Assertions.assertEquals(totalParkingSport - 1, repository.count());
	}

	@Test
	public void deleteDeveriaLancarEntidadeNaoEncontradaExceptionQuandoIdNaoExistir() {
		Assertions.assertThrows(EntidadeNaoEncontradaException.class, () -> {
			service.delete(uuidIncorrect);
		});
	}
}
