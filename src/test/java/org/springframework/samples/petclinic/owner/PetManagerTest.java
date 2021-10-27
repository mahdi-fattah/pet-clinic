package org.springframework.samples.petclinic.owner;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import org.springframework.samples.petclinic.visit.Visit;
import org.springframework.samples.petclinic.utility.PetTimedCache;

import java.util.HashSet;
import java.util.List;
import java.time.LocalDate;
import java.util.Arrays;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class PetManagerTest {
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

	// double: stub(owner), state verification, style: mockisty
	@Test
	public void FindOwnerShouldReturnOwnerIfOwnerExistsInOwnerRepository() {
		Owner mahdi = mock(Owner.class);
		when(owners.findById(1)).thenReturn(mahdi);
		assertEquals(petManager.findOwner(1), mahdi);
	}

	// double: stub(owner), state verification, style: mockisty
	@Test
	public void FindOwnerShouldReturnNullIfOwnerNotExistsInOwnerRepository() {
		Owner mahdi = mock(Owner.class);
		when(owners.findById(1)).thenReturn(mahdi);
		assertNull(petManager.findOwner(2));
	}

	// double: spy(owner), behaviour verification, style: mockisty
	@Test
	public void NewPetShouldCallAddPetOnce() {
		Owner arshiya = spy(Owner.class);
		Pet pet = petManager.newPet(arshiya);
		verify(arshiya, times(1)).addPet(pet);
	}

	// double: stub(PetTimedCache pets), state verification, style: mockisty
	@Test
	public void FindPetShouldReturnPetIfPetExists() {
		Pet dog = mock(Pet.class); // dummy
		when(pets.get(99)).thenReturn(dog);
		assertEquals(petManager.findPet(99), dog);
	}

	// double: stub(PetTimedCache pets), state verification, style: mockisty
	@Test
	public void FindPetShouldReturnNullIfPetNotExists() {
		Pet dog = mock(Pet.class); // dummy
		Pet cat = mock(Pet.class); // dummy
		when(pets.get(99)).thenReturn(dog);
		when(pets.get(101)).thenReturn(cat);
		assertNull(petManager.findPet(100));
	}

	// double: spy(owner), behaviour verification, style: mockisty
	@Test
	public void SavePetShouldCallAddPetAndSaveOnce() {
		Pet tiger = mock(Pet.class); // dummy
		Owner arshiya = spy(Owner.class);
		petManager.savePet(tiger, arshiya);
		verify(arshiya, times(1)).addPet(tiger);
		verify(pets, times(1)).save(tiger);
	}

	// double: stub(OwnerRepository owners), stub(owner), state verification, style: mockisty
	@Test
	public void GetOwnerPetsShouldReturnPetsOfSpecificOwner() {
		List<Pet> mahdisPet = Arrays.asList(mock(Pet.class), mock(Pet.class)); // dummy(Pet)
		Owner mahdi = mock(Owner.class);
		when(mahdi.getPets()).thenReturn(mahdisPet);
		when(owners.findById(7)).thenReturn(mahdi);
		assertEquals(petManager.getOwnerPets(7), mahdisPet);
	}

	// double: stub(OwnerRepository owners), stub(owner), state verification, style: mockisty
	@Test
	public void GetOwnerPetsShouldReturnThrowsNullPointerExceptionIfOwnerNotExists() {
		List<Pet> mahdisPet = Arrays.asList(mock(Pet.class), mock(Pet.class)); // dummy(Pet)
		Owner mahdi = mock(Owner.class);
		when(mahdi.getPets()).thenReturn(mahdisPet);
		when(owners.findById(7)).thenReturn(mahdi);
		assertThrows(NullPointerException.class, () -> petManager.getOwnerPets(8));
	}

	// double: stub(dog), stub(cat), stub(OwnerRepository owners), stub(owner), state verification, style: mockisty
	@Test
	public void GetOwnerPetTypesShouldReturnTypeOfPets() {
		PetType dogType = mock(PetType.class); // dummy
		PetType catType = mock(PetType.class); // dummy
		Pet dog = mock(Pet.class);
		Pet cat = mock(Pet.class);
		when(dog.getType()).thenReturn(dogType);
		when(cat.getType()).thenReturn(catType);
		List<Pet> mahdisPet = Arrays.asList(dog, cat);
		Owner mahdi = mock(Owner.class);
		when(mahdi.getPets()).thenReturn(mahdisPet);
		when(owners.findById(1377)).thenReturn(mahdi);
		HashSet<PetType> expected = new HashSet();
		expected.add(dogType);
		expected.add(catType);
		assertEquals(petManager.getOwnerPetTypes(1377), expected);
	}

	// double: stub(Pet), state verification, style: mockisty
	@Test
	void GetVisitsBetweenShouldReturnsVisitsIBetweenTwoDates() {
		Visit visit0 = mock(Visit.class); // dummy
		Visit visit1 = mock(Visit.class); // dummy
		List<Visit> expectedVisits = Arrays.asList(visit0, visit1);
		Pet pet = mock(Pet.class);
		when(pet.getVisitsBetween(any(LocalDate.class), any(LocalDate.class))).thenReturn(expectedVisits);
		assertEquals(expectedVisits, pet.getVisitsBetween(LocalDate.parse("2021-01-01"), LocalDate.parse("2021-07-07")));
	}

}
