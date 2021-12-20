package org.springframework.samples.petclinic.utility;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.visit.Visit;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PriceCalculatorTest {
	private static final double baseCharge = 100;
	private static final double basePricePerPet = 100;

	private static int INFANT_YEARS = 2;
	private static double RARE_INFANCY_COEF = 1.4;
	private static double BASE_RARE_COEF = 1.2;
	private static int DISCOUNT_MIN_SCORE = 10;
	private static int DISCOUNT_PRE_VISIT = 2;

	private List<Pet> petList;
	private List<Visit> visitList;
	private  Pet pet;
	private  Visit visit;

	@BeforeEach
	void setUp() {
		petList = new ArrayList<>();
		visitList = new ArrayList<>();
		pet = mock(Pet.class);
		visit = mock(Visit.class);
	}

	@Test
	public void testEmptyPetList() {
		assertEquals(0, PriceCalculator.calcPrice(petList, baseCharge, basePricePerPet));
	}
	@Test
	public void testOneInfantPetWithoutVisit() {
		when(pet.getBirthDate()).thenReturn(LocalDate.of(2021, 1, 1));
		petList.add(pet);
		double expected = basePricePerPet * BASE_RARE_COEF * RARE_INFANCY_COEF;

		assertEquals(expected, PriceCalculator.calcPrice(petList, baseCharge, basePricePerPet));
	}

	@Test
	public void testOneInfantPetWithVisit() {

		petList.add(pet);
		visitList.add(visit);

		when(pet.getBirthDate()).thenReturn(LocalDate.of(2021, 1, 1));
		when(visit.getDate()).thenReturn(LocalDate.of(2021, 1, 1));
		when(pet.getVisitsUntilAge(anyInt())).thenReturn(visitList);

		double expected = basePricePerPet * BASE_RARE_COEF * RARE_INFANCY_COEF;

		assertEquals(expected, PriceCalculator.calcPrice(petList, baseCharge, basePricePerPet));
	}

	@Test
	public void testOneNonInfantPetWithoutVisit() {
		when(pet.getBirthDate()).thenReturn(LocalDate.of(2018, 1, 1));
		petList.add(pet);

		double expected = basePricePerPet * BASE_RARE_COEF;

		assertEquals(expected, PriceCalculator.calcPrice(petList, baseCharge, basePricePerPet));
	}

	@Test
	public void testOneNonInfantPetWithVisit() {
		petList.add(pet);
		visitList.add(visit);

		when(pet.getBirthDate()).thenReturn(LocalDate.of(2018, 1, 1));
		when(visit.getDate()).thenReturn(LocalDate.of(2021, 1, 1));
		when(pet.getVisitsUntilAge(anyInt())).thenReturn(visitList);

		double expected = basePricePerPet * BASE_RARE_COEF;

		assertEquals(expected, PriceCalculator.calcPrice(petList, baseCharge, basePricePerPet));
	}

	@Test
	public void testOnePetWithExactInfantYearsWithoutVisit() {
		when(pet.getBirthDate()).thenReturn(LocalDate.now().minusYears(2));
		petList.add(pet);
		double expected = basePricePerPet * BASE_RARE_COEF * RARE_INFANCY_COEF;

		assertEquals(expected, PriceCalculator.calcPrice(petList, baseCharge, basePricePerPet));
	}

	@Test
	public void testPetsWithExactDiscountMinScoreAndWithMoreThan100DaysFormLastVisit() {
		for (int i = 0; i<5; i++){
			petList.add(pet);
		}
		visitList.add(visit);

		when(pet.getBirthDate()).thenReturn(LocalDate.of(2021, 1, 1));
		when(visit.getDate()).thenReturn(LocalDate.now().minusDays(300));
		when(pet.getVisitsUntilAge(anyInt())).thenReturn(visitList);

		long daysFromLastVisit = DAYS.between(visit.getDate(), LocalDate.now());
		double expected = 4 * basePricePerPet * BASE_RARE_COEF * RARE_INFANCY_COEF;
		expected = (expected + baseCharge) * (daysFromLastVisit / 100 + visitList.size());
		expected += basePricePerPet * BASE_RARE_COEF * RARE_INFANCY_COEF;

		assertEquals(expected, PriceCalculator.calcPrice(petList, baseCharge, basePricePerPet));
	}

	@Test
	public void testPetsWithExactDiscountMinScoreAndWithLessThan100DaysFormLastVisit() {
		for (int i = 0; i<5; i++){
			petList.add(pet);
		}
		visitList.add(visit);

		when(pet.getBirthDate()).thenReturn(LocalDate.of(2021, 1, 1));
		when(visit.getDate()).thenReturn(LocalDate.now().minusDays(75));
		when(pet.getVisitsUntilAge(anyInt())).thenReturn(visitList);

		long daysFromLastVisit = DAYS.between(visit.getDate(), LocalDate.now());
		double expected = 4 * basePricePerPet * BASE_RARE_COEF * RARE_INFANCY_COEF;
		expected = (expected * DISCOUNT_PRE_VISIT) + baseCharge;
		expected += basePricePerPet * BASE_RARE_COEF * RARE_INFANCY_COEF;

		assertEquals(expected, PriceCalculator.calcPrice(petList, baseCharge, basePricePerPet));
	}
  
	@Test
	public void testPetsWithMoreThanDiscountMinScoreAndWith100DaysFormLastVisit() {
		for (int i = 0; i<6; i++){
			petList.add(pet);
		}
		visitList.add(visit);

		when(pet.getBirthDate()).thenReturn(LocalDate.of(2021, 1, 1));
		when(visit.getDate()).thenReturn(LocalDate.now().minusDays(100));
		when(pet.getVisitsUntilAge(anyInt())).thenReturn(visitList);

		long daysFromLastVisit = DAYS.between(visit.getDate(), LocalDate.now());
		double expected = 4 * basePricePerPet * BASE_RARE_COEF * RARE_INFANCY_COEF;
		expected = (expected + baseCharge) * (daysFromLastVisit / 100 + visitList.size());
		expected += basePricePerPet * BASE_RARE_COEF * RARE_INFANCY_COEF;
		expected = (expected + baseCharge) * (daysFromLastVisit / 100 + visitList.size());
		expected += basePricePerPet * BASE_RARE_COEF * RARE_INFANCY_COEF;

		assertEquals(expected, PriceCalculator.calcPrice(petList, baseCharge, basePricePerPet));
	}





}
