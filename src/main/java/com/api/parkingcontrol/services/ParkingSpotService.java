package com.api.parkingcontrol.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import com.api.parkingcontrol.controllers.dtos.ParkingSpotInputDto;
import com.api.parkingcontrol.controllers.dtos.ParkingSpotResultDto;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.repositories.ParkingSpotRepository;
import com.api.parkingcontrol.services.exception.EntidadeEmUsoException;
import com.api.parkingcontrol.services.exception.EntidadeNaoEncontradoException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ParkingSpotService {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ParkingSpotRepository parkingSpotRepository;

	public Page<ParkingSpotResultDto> findAll(Pageable pageable) {
		Page<ParkingSpotModel> parkingSpotModel = parkingSpotRepository.findAll(pageable);
		List<ParkingSpotResultDto> parkingSpotResultDtoList = parkingSpotModel.stream()
			.map(p -> modelMapper.map(p, ParkingSpotResultDto.class))
			.collect(Collectors.toList());
		Page<ParkingSpotResultDto> parkingSpotResultDtoPage = new PageImpl<>(parkingSpotResultDtoList, pageable, parkingSpotModel.getTotalElements());
		return parkingSpotResultDtoPage;
	}

	public ParkingSpotResultDto findById(UUID id) {
		ParkingSpotModel parkingSpotModel = parkingSpotRepository.findById(id)
			.orElseThrow(() -> new EntidadeNaoEncontradoException("Local de estacionamento não encontrado!"));
		return modelMapper.map(parkingSpotModel, ParkingSpotResultDto.class);
	}

	@Transactional
	public ParkingSpotResultDto save(ParkingSpotInputDto parkingSpotInputDto) {

		if (existsByLicensePlateCar(parkingSpotInputDto.getLicensePlateCar())) {
			throw new EntidadeEmUsoException("A licença da placa do carro já está em uso!");
		}
		if (existsByParkingSpotNumber(parkingSpotInputDto.getParkingSpotNumber())) {
			throw new EntidadeEmUsoException("A vaga de estacionamento já está em uso!");
		}
		if (existsByApartmentAndBlock(parkingSpotInputDto.getApartment(), parkingSpotInputDto.getBlock())) {
			throw new EntidadeEmUsoException("Vaga de estacionamento já cadastrada para este apartamento/bloco!");
		}

		var parkingSpotModel = modelMapper.map(parkingSpotInputDto, ParkingSpotModel.class);
		parkingSpotModel = parkingSpotRepository.save(parkingSpotModel);

		return modelMapper.map(parkingSpotModel, ParkingSpotResultDto.class);
	}

	@Transactional
	public ParkingSpotResultDto atualizar(UUID id, ParkingSpotInputDto parkingSpotInputDto) {
		ParkingSpotResultDto parkingSpotResultDto = findById(id);
		modelMapper.map(parkingSpotInputDto, parkingSpotResultDto);
		var parkingSpotModel = modelMapper.map(parkingSpotResultDto, ParkingSpotModel.class);
		parkingSpotModel = parkingSpotRepository.save(parkingSpotModel);
		return modelMapper.map(parkingSpotModel, ParkingSpotResultDto.class);
	}

	@Transactional
	public void delete(UUID id) {
		try {
			parkingSpotRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradoException("Local de estacionamento não encontrado!");
		}
	}

	private boolean existsByLicensePlateCar(String licensePlateCar) {
		return parkingSpotRepository.existsByLicensePlateCar(licensePlateCar);
	}

	private boolean existsByParkingSpotNumber(String parkingSpotNumber) {
		return parkingSpotRepository.existsByParkingSpotNumber(parkingSpotNumber);
	}

	private boolean existsByApartmentAndBlock(String apartment, String block) {
		return parkingSpotRepository.existsByApartmentAndBlock(apartment, block);
	}
}
