package org.springframework.samples.petclinic.owner;

import org.junit.Before;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.jupiter.api.Assertions;
import java.util.ArrayList;
import java.util.List;
import static java.util.Arrays.asList;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;

@RunWith(Theories.class)
public class OwnerTestTheory {
	private Owner owner;
	@Before
	public void setup() {
		owner = new Owner();
	}

	@DataPoints
	public static String[] petNames = {"cat", "dog", "hamster", "kangaroo", "leopard"};

	@DataPoints
	public static List<List<String>> petNamesList() {

		List<List<String>> petsList = new ArrayList<>();
		petsList.add(asList("cat", "dog", "leopard", "hamster"));
		petsList.add(asList("kangaroo","dog"));
		petsList.add(asList("hamster", "kangaroo"));
		petsList.add(asList("leopard", "cat"));
		return petsList;
	}

	@DataPoints
	public static boolean[] ignoreNew = {true, false};

	@Theory
	@DisplayName("getPet should return pet if pet exists in pet list")
	public void getPetShouldReturnPetIfPetExistsInPetList(String petName, List<String> petsList) {
		assumeTrue(petsList.size() != 0);
		assumeTrue(petsList.contains(petName));

		for (String pet : petsList) {
			Pet newPet = new Pet();
			newPet.setName(pet);
			owner.addPet(newPet);
		}
		Pet pet = owner.getPet(petName);

		Assertions.assertEquals(petName, pet.getName());
	}

	@Theory
	@DisplayName("getPet should return null if pet does not exist in pet list")
	public void getPetShouldReturnNullIfPetDoesNotExistInPetList(String petName, List<String> petsList) {
		assumeTrue(petsList.size() != 0);
		assumeFalse(petsList.contains(petName));

		for (String pet : petsList) {
			Pet newPet = new Pet();
			newPet.setName(pet);
			owner.addPet(newPet);
		}
		Assertions.assertNull(owner.getPet(petName));
	}

	@Theory
	@DisplayName("getPet should return null if ignoreNew is true")
	public void getPetShouldReturnNullIfIgnoreNewIsTrue(String petName, List<String> petsList, Boolean ignoreNew) {
		assumeTrue(petsList.size() != 0);
		assumeTrue(petsList.contains(petName));
		assumeTrue(ignoreNew);

		for (String pet : petsList) {
			Pet newPet = new Pet();
			newPet.setName(pet);
			owner.addPet(newPet);
		}

		Assertions.assertNull(owner.getPet(petName, ignoreNew));
	}
}
