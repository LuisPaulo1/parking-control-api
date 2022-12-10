package com.api.parkingcontrol.repositories;

import java.util.Optional;
import java.util.UUID;

import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.util.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ParkingSpotRepositoryTests {

	@Autowired
	private ParkingSpotRepository repository;

	private UUID uuid;

	private UUID uuidIncorrect;

	@BeforeEach
	void setUp() throws Exception {
		uuid = UUID.fromString("932eda96-638b-4cad-af31-3a4f91fc3a5e");
		uuidIncorrect = UUID.fromString("932eda96-638b-4cad-af31-3a4f91fc3a20");
	}

	@Test
	public void findByIdDeveriaRetornarParkingSpotModelQuandoIdExistir() {

		Optional<ParkingSpotModel> result = repository.findById(uuid);

		Assertions.assertTrue(result.isPresent());
	}

	@Test
	public void findByIdNaoDeveriaRetornarParkingSpotModelQuandoIdNaoExistir() {

		Optional<ParkingSpotModel> result = repository.findById(uuidIncorrect);

		Assertions.assertFalse(result.isPresent());
	}

	@Test
	public void saveDeveriaPersistirQuandoIdNulo() {

		ParkingSpotModel parkingSpotModel = Factory.createParkingSpotModel();
		parkingSpotModel.setId(null);

		parkingSpotModel = repository.save(parkingSpotModel);
		Optional<ParkingSpotModel> result = repository.findById(parkingSpotModel.getId());

		Assertions.assertNotNull(parkingSpotModel.getId());
		Assertions.assertTrue(result.isPresent());
		Assertions.assertSame(result.get(), parkingSpotModel);
	}

	@Test
	public void deleteDeveriaDeletarParkingSpotModelQuandoIdExistir() {

		repository.deleteById(uuid);

		Optional<ParkingSpotModel> result = repository.findById(uuidIncorrect);

		Assertions.assertFalse(result.isPresent());
	}

	@Test
	public void deleteDeveriaLancarEmptyResultDataAccessExceptionQuandoIdNaoExistir() {

		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			repository.deleteById(uuidIncorrect);
		});
	}


}
