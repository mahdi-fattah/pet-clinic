package org.springframework.samples.petclinic.owner;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import org.springframework.samples.petclinic.utility.PetTimedCache;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class SavePetTest {
	@InjectMocks
	PetManager petManager;

	@Mock
	OwnerRepository owners;

	@Mock
	PetTimedCache pets;

	@Mock
	Logger Logger;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void SavePetShouldCallAddPetOnce() {
		Pet tiger = mock(Pet.class); // dummy
		Owner arshiya = spy(Owner.class);
		petManager.savePet(tiger, arshiya);
		verify(arshiya, times(1)).addPet(tiger);
	}

	@Test
	public void SavePetShouldCallSaveOnce() {
		Pet lion = mock(Pet.class); // dummy
		Owner mahdi_fattah = spy(Owner.class);
		petManager.savePet(lion, mahdi_fattah);
		verify(pets, times(1)).save(lion);
	}

	@Test
	public void SavePetShouldAddCorrectPatToOwner() {
		Pet monkey = new Pet();
		monkey.setName("PrettyMonkey");
		Owner ali99 = new Owner();
		petManager.savePet(monkey, ali99);
		Pet pet = ali99.getPet("PrettyMonkey");
		assertEquals(monkey, pet);
	}

	@Test
	public void SavePetShouldCauseNullPointerExceptionIfPetIsNull() {
		Owner sed = new Owner();
		assertThrows(NullPointerException.class, () -> {
			petManager.savePet(null, sed);
		});
	}

}
