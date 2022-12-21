package com.api.parkingcontrol.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.api.parkingcontrol.controllers.dtos.ParkingSpotInputDto;
import com.api.parkingcontrol.controllers.dtos.ParkingSpotResultDto;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.repositories.ParkingSpotRepository;
import com.api.parkingcontrol.services.exception.EntidadeNaoEncontradaException;
import com.api.parkingcontrol.util.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ParkingSpotServiceTests {

	@InjectMocks
	private ParkingSpotService service;

	@Mock
	private ParkingSpotRepository repository;

	@Spy
	private ModelMapper modelMapper;

	private ParkingSpotInputDto parkingSpotInputDto;

	private ParkingSpotModel parkingSpotModel;

	private PageImpl<ParkingSpotModel> page;

	private Pageable pageable;

	private UUID uuid;

	private UUID uuidIncorrect;

	@BeforeEach
	void setUp() throws Exception {
		uuid = UUID.fromString("932eda96-638b-4cad-af31-3a4f91fc3a5e");
		uuidIncorrect = UUID.fromString("932eda96-638b-4cad-af31-3a4f91fc3a20");
		parkingSpotInputDto = Factory.createParkingSpotInputDto();
		parkingSpotModel = Factory.createParkingSpotModel();
		pageable = PageRequest.of(0, 12);
		page = new PageImpl<>(List.of(parkingSpotModel));

		when(repository.findAll(pageable)).thenReturn(page);

		when(repository.findById(uuid)).thenReturn(Optional.ofNullable(parkingSpotModel));
		when(repository.findById(uuidIncorrect)).thenThrow(EntidadeNaoEncontradaException.class);

		when(repository.save(any())).thenReturn(parkingSpotModel);

		doNothing().when(repository).deleteById(uuid);
		doThrow(EntidadeNaoEncontradaException.class).when(repository).deleteById(uuidIncorrect);
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
	public void deleteNaoDeveriaLancarEntidadeNaoEncontradaExceptionQuandoIdExistir() {
		Assertions.assertDoesNotThrow(() -> {
			service.delete(uuid);
		});
	}

	@Test
	public void deleteDeveriaLancarEntidadeNaoEncontradaExceptionQuandoIdNaoExistir() {
		Assertions.assertThrows(EntidadeNaoEncontradaException.class, () -> {
			service.delete(uuidIncorrect);
		});
	}

}