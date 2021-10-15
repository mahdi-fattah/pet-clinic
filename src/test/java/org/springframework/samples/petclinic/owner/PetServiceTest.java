package org.springframework.samples.petclinic.owner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class PetServiceTest {
	private final PetService petService;
	private static Pet cat, dog, hamster;
	private final int id;
	private final Pet expectedPet;

	public PetServiceTest(int id, Pet pet) {
		this.id = id;
		expectedPet = pet;
		petService = mock(PetService.class);

	}

	void Setup() {
		cat = new Pet();
		cat.setName("cat");

		dog = new Pet();
		dog.setName("dog");

		hamster = new Pet();
		hamster.setName("hamster");

		when(petService.findPet(1)).thenReturn(cat);
		when(petService.findPet(2)).thenReturn(dog);
		when(petService.findPet(3)).thenReturn(hamster);
	}



	@Parameters
	public static Collection<Object[]> pets() {
		cat = new Pet();
		cat.setName("cat");

		dog = new Pet();
		dog.setName("dog");

		hamster = new Pet();
		hamster.setName("hamster");

		return Arrays.asList (new Object [][]{
			{1, cat},
			{2, dog},
			{3, hamster}
		});
	}

	@Test
	public void Pets_are_found_correctly() {
		Setup();

		assertEquals(expectedPet.getName(), petService.findPet(id).getName());
	}
}
