package org.springframework.samples.petclinic.model.priceCalculators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.UserType;

import org.joda.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CustomerDependentPriceCalculatorTest {
	private CustomerDependentPriceCalculator customerDependentPriceCalculator;
	private List<Pet> pets;
	private Pet pet1,pet2,pet3,pet4,pet5,pet6;
	private PetType rareType;
	private PetType nonRareType;
	private double baseCharge, basePricePerPet;

	@BeforeEach
	public void setup() {
		customerDependentPriceCalculator = new CustomerDependentPriceCalculator();
		pets = new ArrayList<>();
		baseCharge = 100;
		basePricePerPet = 1000;
		pet1 = new Pet();
		pet2 = new Pet();
		pet3 = new Pet();
		pet4 = new Pet();
		pet5 = new Pet();
		pet6 = new Pet();
		rareType = mock(PetType.class);
		nonRareType = mock(PetType.class);
	}

	@Test
	public void calcPriceOfInfantRarePetsWithoutDiscountMinScoreForNewUser() {
		pet1.setType(rareType);
		pet1.setBirthDate(new LocalDate(2020, 1, 1).toDate());
		when(rareType.getRare()).thenReturn(true);
		pets.add(pet1);
		double totalPrice, expected;
		expected = basePricePerPet * 1.2 * 1.4;
		totalPrice = customerDependentPriceCalculator.calcPrice(pets, baseCharge, basePricePerPet, UserType.NEW);
		assertEquals(expected, totalPrice);
	}
	@Test
	public void calcPriceOfRarePetsWithoutDiscountMinScoreForGoldUser() {
		pet1.setType(rareType);
		pet1.setBirthDate(new LocalDate(2015, 1, 1).toDate());
		when(rareType.getRare()).thenReturn(true);
		pets.add(pet1);
		double totalPrice, expected;
		expected = basePricePerPet * 1.2 * UserType.GOLD.discountRate + baseCharge;
		totalPrice = customerDependentPriceCalculator.calcPrice(pets, baseCharge, basePricePerPet, UserType.GOLD);
		assertEquals(expected, totalPrice);
	}

	@Test
	public void calcPriceOfInfantNonRarePetsWithoutDiscountMinScoreForNewUser() {
		pet1.setType(nonRareType);
		pet1.setBirthDate(new LocalDate(2020, 1, 1).toDate());
		when(nonRareType.getRare()).thenReturn(false);
		pets.add(pet1);
		double totalPrice, expected;
		expected = basePricePerPet * 1.2;
		totalPrice = customerDependentPriceCalculator.calcPrice(pets, baseCharge, basePricePerPet, UserType.NEW);
		assertEquals(expected, totalPrice);
	}

	@Test
	public void calcPriceWithDiscountMinScoreForNewUser() {
		pet1.setType(rareType);
		pet2.setType(rareType);
		pet3.setType(rareType);
		pet4.setType(rareType);
		pet5.setType(rareType);
		pet6.setType(nonRareType);

		pet1.setBirthDate(new LocalDate(2020, 1, 1).toDate());
		pet2.setBirthDate(new LocalDate(2020, 1, 1).toDate());
		pet3.setBirthDate(new LocalDate(2020, 1, 1).toDate());
		pet4.setBirthDate(new LocalDate(2020, 1, 1).toDate());
		pet5.setBirthDate(new LocalDate(2020, 1, 1).toDate());
		pet6.setBirthDate(new LocalDate(2015, 1, 1).toDate());

		when(rareType.getRare()).thenReturn(true);
		when(nonRareType.getRare()).thenReturn(false);

		pets.add(pet1);
		pets.add(pet2);
		pets.add(pet3);
		pets.add(pet4);
		pets.add(pet5);
		pets.add(pet6);

		double totalPrice, expected = 0;
		for(int i=0; i<5; i++)
			expected = expected + (basePricePerPet * 1.2 * 1.4);
		expected += basePricePerPet;
		expected = expected * UserType.NEW.discountRate + baseCharge;

		totalPrice = customerDependentPriceCalculator.calcPrice(pets, baseCharge, basePricePerPet, UserType.NEW);
		assertEquals(expected, totalPrice);
	}

	@Test
	public void calcPriceWithDiscountMinScoreForSilverUser() {
		pet1.setType(rareType);
		pet2.setType(rareType);
		pet3.setType(rareType);
		pet4.setType(rareType);
		pet5.setType(rareType);

		pet1.setBirthDate(new LocalDate(2020, 1, 1).toDate());
		pet2.setBirthDate(new LocalDate(2020, 1, 1).toDate());
		pet3.setBirthDate(new LocalDate(2020, 1, 1).toDate());
		pet4.setBirthDate(new LocalDate(2020, 1, 1).toDate());
		pet5.setBirthDate(new LocalDate(2020, 1, 1).toDate());

		when(rareType.getRare()).thenReturn(true);

		pets.add(pet1);
		pets.add(pet2);
		pets.add(pet3);
		pets.add(pet4);
		pets.add(pet5);

		double totalPrice, expected = 0;
		for(int i=0; i<5; i++)
			expected = expected + (basePricePerPet * 1.2 * 1.4);
		expected = (expected + baseCharge) * UserType.SILVER.discountRate;
		totalPrice = customerDependentPriceCalculator.calcPrice(pets, baseCharge, basePricePerPet, UserType.SILVER);
		assertEquals(expected, totalPrice);
	}

}
