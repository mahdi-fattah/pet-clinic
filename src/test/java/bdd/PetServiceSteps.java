package bdd;


import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.owner.*;
import org.slf4j.Logger;
import org.springframework.samples.petclinic.utility.PetTimedCache;


import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PetServiceSteps {

	@Autowired
	OwnerRepository ownerRepository;

	@Autowired
	PetRepository petRepository;

	@Autowired
	PetTypeRepository petTypeRepository;

	@Autowired
	PetTimedCache petTimedCache;

	@Autowired
	PetService petService;

	Logger logger = Mockito.mock(Logger.class);


	private Owner owner;
	private Owner found_owner;
	private Pet pet;
	private Pet found_pet;
	private Pet new_pet;


	@Before("@petService_annotation")
	public void setUp() {
		petTimedCache = new PetTimedCache(petRepository);
		petService = new PetService(petTimedCache, ownerRepository, logger);
	}


	@Given("There is a owner called {string}")
	public void thereIsAOwnerCalled(String name) {
		owner = new Owner();
		owner.setId(1);
		owner.setFirstName("Mahdi");
		owner.setLastName("Fat");
		owner.setAddress("bagherabad - Kooche bahar");
		owner.setCity("qarchak");
		owner.setTelephone("09187652134");
		ownerRepository.save(owner);
	}

	@When("performs find owner")
	public void PerformFindOwner() {
		found_owner = petService.findOwner(1);
	}

	@Then("the logger for findOwner will be called correctly")
	public void loggerForFindOwnerCorrect() {
		Mockito.verify(logger).info("find owner {}", owner.getId());
	}

	@And("the owner will be returned successfully")
	public void FindOwnerCorrect() {
		Assertions.assertEquals(owner.getId(), found_owner.getId());
		Assertions.assertEquals(owner.getAddress(),found_owner.getAddress());
		Assertions.assertEquals(owner.getFirstName(), found_owner.getFirstName());
		Assertions.assertEquals(owner.getLastName(), found_owner.getLastName());
		Assertions.assertEquals(owner.getTelephone(), found_owner.getTelephone());
	}

	@Given("There is a pet called {string}")
	public void thereIsAPetCalled(String name) {
		pet = new Pet();
		pet.setId(1);
		PetType petType = new PetType();
		petType.setId(1);
		petType.setName("test pet_type");
		pet.setName(name);
		pet.setBirthDate(LocalDate.parse("2021-01-01"));
		pet.setType(petType);
		owner = new Owner();
		owner.setId(1);
		owner.addPet(pet);
		petTimedCache.save(pet);
	}

	@When("performs find pet")
	public void PerformFindPet() {
		found_pet = petService.findPet(1);
	}

	@Then("the logger for findPet will be called correctly")
	public void loggerForFindPetCorrect() {
		Mockito.verify(logger).info("find pet by id {}", pet.getId());
	}

	@And("the pet will be returned successfully")
	public void FindPetCorrect() {
		Assertions.assertEquals(pet.getId(), found_pet.getId());
		Assertions.assertEquals(pet.getName(), found_pet.getName());
		Assertions.assertEquals(pet.getBirthDate(), found_pet.getBirthDate());
	}

	@When("performs save pet service to add a pet to his list")
	public void hePerformsSavePetService() {
		pet = new Pet();
		PetType petType = new PetType();
		petType.setId(1);
		petType.setName("dog");
		pet.setName("new pet");
		pet.setId(1);
		pet.setType(petType);
		petService.savePet(pet, owner);
	}

	@Then("the logger for savePet will be called correctly")
	public void loggerForSavePetCorrect() {
		Mockito.verify(logger).info("save pet {}", pet.getId());
	}

	@And("the pet add to owner")
	public void petAddToOwner() {
		assertNotNull(owner.getPets());
	}

	@Then("pet is saved successfully")
	public void petIsSaved() {
		assertNotNull(petService.findPet(1));
		assertEquals(1 , petService.findPet(1).getOwner().getId());
	}

	@When("performs new pet")
	public void PerformFNewPet() {
		new_pet = petService.newPet(owner);
	}

	@Then("the logger for newPet will be called correctly")
	public void loggerForNewPetCorrect() {
		Mockito.verify(logger).info("add pet for owner {}", owner.getId());
	}

	@And("empty pet added to owner")
	public void testNewPet(){
		Assertions.assertNotNull(owner.getPets());
	}

}
