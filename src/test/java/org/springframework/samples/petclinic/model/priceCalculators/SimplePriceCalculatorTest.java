package org.springframework.samples.petclinic.model.priceCalculators;

import org.junit.Before;
import org.junit.Test;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.UserType;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class SimplePriceCalculatorTest {
	private SimplePriceCalculator simplePriceCalculator;
	private List<Pet> pets;
	private double baseCharge, basePricePerPet;
	private Pet pet1;
	private PetType rareType;
	private PetType usualType;

	@Before
	public void setup() {
		simplePriceCalculator = new SimplePriceCalculator();
		pets = new ArrayList<>();
		baseCharge = 100;
		basePricePerPet = 1000;
		pet1 = mock(Pet.class);
		rareType = mock(PetType.class);
		usualType = mock(PetType.class);
	}


	@Test
	public void calcPriceOfRarePetForNewUser() {
		when(pet1.getType()).thenReturn(rareType);
		when(rareType.getRare()).thenReturn(true);
		pets.add(pet1);
		double totalPrice, expected;
		expected = (baseCharge + basePricePerPet * 1.2) * UserType.NEW.discountRate;
		totalPrice = simplePriceCalculator.calcPrice(pets, baseCharge, basePricePerPet, UserType.NEW);
		assertEquals(expected, totalPrice);
	}

	@Test
	public void calcPriceOfUsualPetForGoldUser() {
		when(pet1.getType()).thenReturn(usualType);
		when(usualType.getRare()).thenReturn(false);
		pets.add(pet1);
		double totalPrice, expected;
		expected = baseCharge + basePricePerPet;
		totalPrice = simplePriceCalculator.calcPrice(pets, baseCharge, basePricePerPet, UserType.GOLD);
		assertEquals(expected, totalPrice);
	}
}
