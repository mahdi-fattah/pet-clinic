package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.*;

import java.util.*;

class OwnerTest {
	private Owner owner;
	private Pet cat, dog, kangaroo, leopard;

	@BeforeEach
	void setup() {
		owner = new Owner();
		cat = new Pet();
		dog = new Pet();
		kangaroo = new Pet();
		leopard = new Pet();
		dog.setName("dog");
		cat.setName("cat");
		kangaroo.setName("kangaroo");
		leopard.setName("leopard");
	}

	@AfterEach
	void teardown() {
		owner = null;
	}

	@Test
	@DisplayName("set and get address should work")
	public void testSetAndGetAddress() {
		owner.setAddress("address_");
		Assertions.assertEquals(owner.getAddress(), "address_");
	}

	@Test
	@DisplayName("set and get telephone should work")
	public void testSetAndGetTelephone() {
		owner.setTelephone("09030102030");
		Assertions.assertEquals(owner.getTelephone(), "09030102030");
	}

	@Test
	@DisplayName("set and get city should work")
	public void testSetAndGetCity() {
		owner.setCity("city_");
		Assertions.assertEquals(owner.getCity(), "city_");
	}

	@Test
	@DisplayName("owner should be set after add pet")
	public void testOwnerSetAfterAddPet() {
		owner.addPet(kangaroo);
		Assertions.assertEquals(owner, kangaroo.getOwner());
	}

	@Test
	@DisplayName("Get Pets should returns pets is descending order")
	void GetPetsShouldReturnsPetsIsDescendingOrder() {
		owner.addPet(cat);
		owner.addPet(leopard);
		owner.addPet(dog);
		owner.addPet(kangaroo);

		List<Pet> expectedPets = new ArrayList<>();
		expectedPets.add(cat);
		expectedPets.add(dog);
		expectedPets.add(kangaroo);
		expectedPets.add(leopard);

		List<Pet> retrievedPets = owner.getPets();
		Assertions.assertEquals(expectedPets, retrievedPets);
	}

	@Test
	@DisplayName("Add pet should add new pet in petslist when pet is new and has ID")
	void addPetShouldAddNewPetInPetsListWhenEPetIsNewAndHasID() {
		dog.setId(666);

		owner.addPet(cat);
		owner.addPet(dog);
		owner.addPet(kangaroo);
		owner.addPet(leopard);

		Assertions.assertEquals(3, owner.getPets().size());
	}

	@Test
	@DisplayName("addPet should cause NullPointerException if pet is null")
	void addPetShouldCauseNullPointerExceptionIfPetIsNull() {
		Assertions.assertThrows(NullPointerException.class, () -> {
			owner.addPet(null);
		});
	}

	@Test
	@DisplayName("removePet should remove pet from pets list")
	void removePetShouldRemovePetFromPetsList() {
		owner.addPet(cat);
		owner.addPet(dog);
		owner.addPet(leopard);

		List<Pet> expectedPets = new ArrayList<>();
		expectedPets.add(cat);
		expectedPets.add(leopard);

		owner.removePet(dog);
		Assertions.assertEquals(expectedPets, owner.getPets());
	}

	@Test
	@DisplayName("removePet should do nothing if pet was null")
	void removePetShouldDoNothingIfPetWasNull() {
		owner.addPet(cat);
		owner.addPet(kangaroo);

		List<Pet> expectedPets = new ArrayList<>();
		expectedPets.add(cat);
		expectedPets.add(kangaroo);

		owner.removePet(leopard);
		Assertions.assertEquals(expectedPets, owner.getPets());
	}

	@Test
	@DisplayName("removePet should do nothing if pet does not exist in pets list")
	void removePetShouldDoNothingIfPetDoesNotExistInPetsList() {
		owner.addPet(cat);
		owner.addPet(leopard);

		List<Pet> expectedPets = new ArrayList<>();
		expectedPets.add(cat);
		expectedPets.add(leopard);

		owner.removePet(null);
		Assertions.assertEquals(expectedPets, owner.getPets());
	}

	@Test
	@DisplayName("getPet should return pet if pet exists in pet list")
	void getPetShouldReturnPetIfPetExistsInPetList() {
		kangaroo.setId(123);

		owner.addPet(dog);
		owner.addPet(kangaroo);
		owner.addPet(cat);

		dog.setId(7);

		Assertions.assertEquals(owner.getPet("cat"), cat);
		Assertions.assertEquals(owner.getPet("dog"), dog);
		Assertions.assertNull(owner.getPet("kangaroo"));
	}

	@Test
	@DisplayName("getPet should return null if pet does not exist in pet list")
	void getPetShouldReturnNullIfPetDoesNotExistInPetList() {
		owner.addPet(cat);
		owner.addPet(dog);

		Assertions.assertNull(owner.getPet("leopard"));
	}

	@Test
	@DisplayName("getPet should return null if ignoreNew is true")
	void getPetShouldReturnPetIfIgnoreNewIsTrue() {
		cat.setId(15);

		owner.addPet(cat);
		owner.addPet(dog);

		Assertions.assertNull(owner.getPet("leopard", true));
		Assertions.assertNull(owner.getPet("cat", true));
		Assertions.assertNull(owner.getPet("dog", true));
	}
}
