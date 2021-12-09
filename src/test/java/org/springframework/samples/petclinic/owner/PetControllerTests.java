package org.springframework.samples.petclinic.owner;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.utility.PetTimedCache;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.mockito.BDDMockito.given;

@WebMvcTest(value = PetController.class,
	includeFilters = {
		@ComponentScan.Filter(value = PetTypeFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
		@ComponentScan.Filter(value = PetService.class, type = FilterType.ASSIGNABLE_TYPE),
		@ComponentScan.Filter(value = LoggerConfig.class, type = FilterType.ASSIGNABLE_TYPE),
		@ComponentScan.Filter(value = PetTimedCache.class, type = FilterType.ASSIGNABLE_TYPE),
	}
)
class PetControllerTests {
	private static final String VIEWS_PETS_CREATE_OR_UPDATE_FORM = "pets/createOrUpdatePetForm";
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PetRepository pets;

	@MockBean
	private OwnerRepository owners;

	@BeforeEach
	void setup() {
		PetType dog = new PetType();
		dog.setName("dog");
		dog.setId(10);

		Pet pet = new Pet();
		pet.setId(11);
		pet.setName("spot");
		given(owners.findById(1)).willReturn(new Owner());
		given(pets.findPetTypes()).willReturn(Lists.newArrayList(dog));
		given(pets.findById(11)).willReturn(pet);
	}

	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/owners/1/pets/new"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("pet"))
			.andExpect(view().name(VIEWS_PETS_CREATE_OR_UPDATE_FORM));
	}

	@Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(
				post("/owners/1/pets/new")
					.param("name", "spot")
					.param("type", "dog")
					.param("birthDate", "2019-03-01"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/owners/{ownerId}"));
	}
	@Test
	void testProcessCreationFormWithError() throws Exception {
		mockMvc.perform(
			post("/owners/1/pets/new")
				.param("name", "spot")
				.param("birthDate", "2019-03-01"))
			.andExpect(model().attributeHasNoErrors("owner"))
			.andExpect(model().attributeHasErrors("pet")).andExpect(model().attributeHasFieldErrors("pet", "type"))
			.andExpect(model().attributeHasFieldErrorCode("pet", "type", "required"))
			.andExpect(status().isOk())
			.andExpect(view().name(VIEWS_PETS_CREATE_OR_UPDATE_FORM));
	}

	@Test
	void testInitUpdateForm() throws Exception {
		mockMvc.perform(
				get("/owners/1/pets/11/edit"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("pet"))
			.andExpect(view().name(VIEWS_PETS_CREATE_OR_UPDATE_FORM));
	}

	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		mockMvc.perform(
				post("/owners/1/pets/11/edit")
					.param("name", "spot")
					.param("type", "dog")
					.param("birthDate", "2019-03-01"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/owners/{ownerId}"));
	}

	@Test
	void testProcessUpdateFormWithError() throws Exception {
		mockMvc.perform(
				post("/owners/1/pets/11/edit")
					.param("name", "spot")
					.param("birthDate", "2019-03-01"))
			.andExpect(model().attributeHasNoErrors("owner"))
			.andExpect(model().attributeHasErrors("pet"))
			.andExpect(model().attributeHasFieldErrors("pet", "type"))
			.andExpect(model().attributeHasFieldErrorCode("pet", "type", "required"))
			.andExpect(status().isOk())
			.andExpect(view().name(VIEWS_PETS_CREATE_OR_UPDATE_FORM));
	}
}
