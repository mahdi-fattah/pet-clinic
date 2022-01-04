@petService_annotation
Feature: PetService Feature

  Scenario: First Scenario FindOwner
    Given There is a owner called "Mahdi Fat"
    When performs find owner
    Then the logger for findOwner will be called correctly
    And  the owner will be returned successfully


  Scenario: Second Scenario FindPet
    Given There is a pet called "test pet"
    When performs find pet
    Then the logger for findPet will be called correctly
    And the pet will be returned successfully

  Scenario: Third Scenario savePet
    Given There is a owner called "Mahdi Fat"
    When performs save pet service to add a pet to his list
    Then the logger for savePet will be called correctly
    And the pet add to owner
    And pet is saved successfully


  Scenario: forth Scenario newPet
    Given There is a owner called "Mahdi Fat"
    When performs new pet
    Then the logger for newPet will be called correctly
    And empty pet added to owner
