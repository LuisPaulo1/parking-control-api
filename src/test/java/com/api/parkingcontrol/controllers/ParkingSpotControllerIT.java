package com.api.parkingcontrol.controllers;

import java.util.UUID;

import com.api.parkingcontrol.controllers.dtos.ParkingSpotInputDto;
import com.api.parkingcontrol.util.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ParkingSpotControllerIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private String adminName;

	private String password;

	private String passwordIncorrect;

	private String userName;

	private UUID uuid;

	private ParkingSpotInputDto parkingSpotInputDto;

	private UUID uuidIncorrect;

	@BeforeEach
	void setUp() throws Exception {
		adminName = "maria";
		userName = "joao";
		password = "123456";
		passwordIncorrect = "789456";
		uuid = UUID.fromString("932eda96-638b-4cad-af31-3a4f91fc3a5e");
		uuidIncorrect = UUID.fromString("932eda96-638b-4cad-af31-3a4f91fc3a20");
		parkingSpotInputDto = Factory.createParkingSpotInputDto();
	}

	@Test
	void getAllParkingSpotsDeveriaRetornarOsRecursosComStatusOk() throws Exception {
		ResultActions result =
			mockMvc.perform(get("/parking-spot")
				.with(httpBasic(adminName, password))
				.contentType(MediaType.APPLICATION_JSON));

		result.andExpectAll(status().isOk());
	}

	@Test
	void getAllParkingSpotsDeveriaRetornarUnauthorizedQuandoPasswordEstiverIncorreto() throws Exception {
		ResultActions result =
			mockMvc.perform(get("/parking-spot")
				.with(httpBasic(adminName, passwordIncorrect))
				.contentType(MediaType.APPLICATION_JSON));

		result.andExpectAll(status().isUnauthorized());
	}

	@Test
	void getOneParkingSpotDeveriaRetornarStatusOkQuandoUUIDExistir() throws Exception {
		ResultActions result =
			mockMvc.perform(get("/parking-spot/{id}", uuid)
				.with(httpBasic(userName, password))
				.contentType(MediaType.APPLICATION_JSON));

		result.andExpectAll(status().isOk());
	}

	@Test
	void getOneParkingSpotDeveriaRetornarStatusNotFoundQuandoUUIDEStiverIncorreto() throws Exception {
		ResultActions result =
			mockMvc.perform(get("/parking-spot/{id}", uuidIncorrect)
				.with(httpBasic(userName, password))
				.contentType(MediaType.APPLICATION_JSON));

		result.andExpectAll(status().isNotFound());
	}

	@Test
	public void saveParkingSpotDeveriaInserirRecursoERetornarStatusCreatedQuandoUsuarioTemPermisaoDeAdmin() throws Exception {

		String jsonBody = objectMapper.writeValueAsString(parkingSpotInputDto);

		ResultActions result =
			mockMvc.perform(post("/parking-spot")
				.with(httpBasic(adminName, password))
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isCreated());
	}

	@Test
	public void saveParkingSpotDeveriaRetornarStatusForbiddenQuandoUsuarioNaoTemPermisaoDeAdmin() throws Exception {

		String jsonBody = objectMapper.writeValueAsString(parkingSpotInputDto);

		ResultActions result =
			mockMvc.perform(post("/parking-spot")
				.with(httpBasic(userName, password))
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isForbidden());
	}

	@Test
	public void saveParkingSpotDeveriaRetornarStatusUnauthorizedQuandoUsuarioNaoEstaLogado() throws Exception {

		String jsonBody = objectMapper.writeValueAsString(parkingSpotInputDto);

		ResultActions result =
			mockMvc.perform(post("/parking-spot")
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isUnauthorized());
	}

	@Test
	public void saveParkingSpotDeveriaRetornarStatusBadRequestQuandoCampoParkingSpotNumberNaoEInformado() throws Exception {

		ParkingSpotInputDto parkingSpotInputDto = new ParkingSpotInputDto(null, "ABC8123", "Fiat", "cvb", "preto", "Carlos", "300", "G");
		String jsonBody = objectMapper.writeValueAsString(parkingSpotInputDto);

		ResultActions result =
			mockMvc.perform(post("/parking-spot")
				.with(httpBasic(adminName, password))
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isBadRequest());
	}

	@Test
	public void saveParkingSpotDeveriaRetornarStatusBadRequestQuandoCampoLicensePlateCarForMaiorQueSete() throws Exception {

		ParkingSpotInputDto parkingSpotInputDto = new ParkingSpotInputDto("44AD", "ABC81235", "Fiat", "cvb", "preto", "Carlos", "300", "G");
		String jsonBody = objectMapper.writeValueAsString(parkingSpotInputDto);

		ResultActions result =
			mockMvc.perform(post("/parking-spot")
				.with(httpBasic(adminName, password))
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isBadRequest());
	}

	@Test
	public void saveParkingSpotDeveriaRetornarStatusConflictQuandoCampoLicensePlateCarEstiverCadastrado() throws Exception {

		ParkingSpotInputDto parkingSpotInputDto = new ParkingSpotInputDto("44AD", "RRS8562", "Fiat", "cvb", "preto", "Carlos", "300", "G");
		String jsonBody = objectMapper.writeValueAsString(parkingSpotInputDto);

		ResultActions result =
			mockMvc.perform(post("/parking-spot")
				.with(httpBasic(adminName, password))
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isConflict());
	}

	@Test
	public void saveParkingSpotDeveriaRetornarStatusConflictQuandoCampoParkingSpotNumberEstiverCadastrado() throws Exception {

		ParkingSpotInputDto parkingSpotInputDto = new ParkingSpotInputDto("205B", "ABC8123", "Fiat", "cvb", "preto", "Carlos", "300", "G");
		String jsonBody = objectMapper.writeValueAsString(parkingSpotInputDto);

		ResultActions result =
			mockMvc.perform(post("/parking-spot")
				.with(httpBasic(adminName, password))
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isConflict());
	}

	@Test
	public void updateParkingSpotDeveriaAtualizarRecursoERetornarStatusOkQuandoIdExistir() throws Exception {

		String jsonBody = objectMapper.writeValueAsString(parkingSpotInputDto);

		ResultActions result =
			mockMvc.perform(put("/parking-spot/{id}", uuid)
				.with(httpBasic(adminName, password))
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
	}

	@Test
	public void updateParkingSpotDeveriaRetornarStatusNotFoundQuandoIdNaoExistir() throws Exception {

		String jsonBody = objectMapper.writeValueAsString(parkingSpotInputDto);

		ResultActions result =
			mockMvc.perform(put("/parking-spot/{id}", uuidIncorrect)
				.with(httpBasic(adminName, password))
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());
	}

	@Test
	public void deleteParkingSpotDeveriaRetornarStatusOkQuandoIdExistir() throws Exception {
		ResultActions result =
			mockMvc.perform(delete("/parking-spot/{id}", uuid)
				.with(httpBasic(adminName, password)));

		result.andExpect(status().isOk());
	}

	@Test
	public void deleteParkingSpotDeveriaRetornarStatusNotFoundQuandoIdNaoExistir() throws Exception {
		ResultActions result =
			mockMvc.perform(delete("/parking-spot/{id}", uuidIncorrect)
				.with(httpBasic(adminName, password)));

		result.andExpect(status().isNotFound());
	}

}
