package com.api.parkingcontrol.controllers;

import java.util.List;
import java.util.UUID;

import com.api.parkingcontrol.controllers.dtos.ParkingSpotInputDto;
import com.api.parkingcontrol.controllers.dtos.ParkingSpotResultDto;
import com.api.parkingcontrol.services.ParkingSpotService;
import com.api.parkingcontrol.services.exception.EntidadeNaoEncontradaException;
import com.api.parkingcontrol.util.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ParkingSpotController.class)
public class ParkingSpotControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ParkingSpotService service;

	@Autowired
	private ObjectMapper objectMapper;

	private final String adminName = "maria";

	private final String userName =  "joao";

	private final String password = "123456";

	private UUID uuid;

	private UUID uuidIncorrect;

	private ParkingSpotInputDto parkingSpotInputDto;

	private ParkingSpotResultDto parkingSpotResultDto;

	private PageImpl<ParkingSpotResultDto> page;

	@BeforeEach
	void setUp() throws Exception {
		uuid = UUID.fromString("932eda96-638b-4cad-af31-3a4f91fc3a5e");
		uuidIncorrect = UUID.fromString("932eda96-638b-4cad-af31-3a4f91fc3a20");
		parkingSpotInputDto = Factory.createParkingSpotInputDto();
		parkingSpotResultDto = Factory.createParkingSpotResultDto();
		page = new PageImpl<>(List.of(parkingSpotResultDto));

		when(service.findAll(any())).thenReturn(page);

		when(service.findById(uuid)).thenReturn(parkingSpotResultDto);
		when(service.findById(uuidIncorrect)).thenThrow(EntidadeNaoEncontradaException.class);

		when(service.save(parkingSpotInputDto)).thenReturn(parkingSpotResultDto);

		when(service.update(eq(uuid), any())).thenReturn(parkingSpotResultDto);
		when(service.update(eq(uuidIncorrect), any())).thenThrow(EntidadeNaoEncontradaException.class);

		doNothing().when(service).delete(uuid);
		doThrow(EntidadeNaoEncontradaException.class).when(service).delete(uuidIncorrect);
	}

	@Test
	@WithMockUser(username = adminName, password = password, roles = "ADMIN")
	void getAllParkingSpotsDeveriaRetornarOsRecursosComStatusOk() throws Exception {
		ResultActions result =
			mockMvc.perform(get("/parking-spot")
				.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = userName, password = password, roles = "USER")
	void getOneParkingSpotDeveriaRetornarStatusOkQuandoUUIDExistir() throws Exception {
		ResultActions result =
			mockMvc.perform(get("/parking-spot/{id}", uuid)
				.contentType(MediaType.APPLICATION_JSON));

		result.andExpectAll(status().isOk());
	}

	@Test
	@WithMockUser(username = userName, password = password, roles = "USER")
	void getOneParkingSpotDeveriaRetornarStatusNotFoundQuandoUUIDEStiverIncorreto() throws Exception {
		ResultActions result =
			mockMvc.perform(get("/parking-spot/{id}", uuidIncorrect)
				.contentType(MediaType.APPLICATION_JSON));

		result.andExpectAll(status().isNotFound());
	}

	@Test
	public void saveParkingSpotDeveriaInserirRecursoERetornarStatusCreatedQuandoUsuarioTemPermisaoDeAdmin() throws Exception {

		String jsonBody = objectMapper.writeValueAsString(parkingSpotInputDto);

		ResultActions result =
			mockMvc.perform(post("/parking-spot")
				.with(SecurityMockMvcRequestPostProcessors.user(adminName).roles("ADMIN"))
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isCreated());
	}

	@Test
	public void updateParkingSpotDeveriaAtualizarRecursoERetornarStatusOkQuandoIdExistir() throws Exception {

		String jsonBody = objectMapper.writeValueAsString(parkingSpotInputDto);

		ResultActions result =
			mockMvc.perform(put("/parking-spot/{id}", uuid)
				.with(SecurityMockMvcRequestPostProcessors.user(adminName).roles("ADMIN"))
				.with(SecurityMockMvcRequestPostProcessors.csrf())
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
				.with(SecurityMockMvcRequestPostProcessors.user(adminName).roles("ADMIN"))
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());
	}

	@Test
	public void deleteParkingSpotDeveriaRetornarStatusOkQuandoIdExistir() throws Exception {
		ResultActions result =
			mockMvc.perform(delete("/parking-spot/{id}", uuid)
				.with(SecurityMockMvcRequestPostProcessors.user(adminName).roles("ADMIN"))
				.with(SecurityMockMvcRequestPostProcessors.csrf()));

		result.andExpect(status().isOk());
	}

	@Test
	public void deleteParkingSpotDeveriaRetornarStatusNotFoundQuandoIdNaoExistir() throws Exception {
		ResultActions result =
			mockMvc.perform(delete("/parking-spot/{id}", uuidIncorrect)
				.with(SecurityMockMvcRequestPostProcessors.user(adminName).roles("ADMIN"))
				.with(SecurityMockMvcRequestPostProcessors.csrf()));

		result.andExpect(status().isNotFound());
	}

}
