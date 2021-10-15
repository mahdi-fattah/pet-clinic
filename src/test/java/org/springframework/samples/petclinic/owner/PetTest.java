package org.springframework.samples.petclinic.owner;

import org.junit.Before;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.jupiter.api.*;

import org.junit.runner.RunWith;
import org.springframework.samples.petclinic.visit.Visit;

import java.time.LocalDate;
import java.util.*;
import org.junit.jupiter.api.Assertions;
import static org.junit.Assume.assumeTrue;

@RunWith(Theories.class)
public class PetTest {
	private Pet pet;

	@Before
	public void setup() {
		pet = new Pet();
	}

	@DataPoints
	public static List[] visits = {
		Arrays.asList(
			new Visit().setDate(LocalDate.parse("2021-11-01")),
			new Visit().setDate(LocalDate.parse("2021-09-20"))
		),
		Arrays.asList(
			new Visit().setDate(LocalDate.parse("2021-08-22")),
			new Visit().setDate(LocalDate.parse("2021-05-16"))
		),
		Arrays.asList(
			new Visit().setDate(LocalDate.parse("2021-04-04"))
		)
	};
	@Theory
	@DisplayName("getVisits should returns visits is descending order")
	public void getVisitsShouldReturnsVisitsInDescendingOrder(List<Visit> visits) {
		assumeTrue(visits.size() != 0 );
		for (Visit visit : visits)
			pet.addVisit(visit);

		List<Visit> retrievedVisits = pet.getVisits();

		LocalDate previousDate = LocalDate.parse("3000-01-01");
		boolean sorted = true;
		for (Visit visit : retrievedVisits) {
			if(visit.getDate().isAfter(previousDate)){
				sorted = false;
				break;
			}
			previousDate = visit.getDate();
		}
		Assertions.assertTrue(sorted);
	}

	@Theory
	@DisplayName("getVisits should returns all visits")
	public void getVisitsShouldReturnAllVisits(List<Visit> visits) {
		assumeTrue(visits.size() != 0);
		for (Visit visit : visits)
			pet.addVisit(visit);

		List<Visit> retrievedVisits = pet.getVisits();
		for (Visit v : visits) {
			Assertions.assertTrue(retrievedVisits.contains(v));
		}
	}

}

