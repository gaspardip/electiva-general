package com.um.mascotas.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.um.mascotas.model.PetModel
import com.um.mascotas.model.PetProvider

class PetViewModel: ViewModel() {
    val petModel = MutableLiveData<ArrayList<PetModel>>(arrayListOf<PetModel>(PetProvider.getRandomPet()));

    fun addRandomPet() {
        println("hi from add pet");
        val petToAdd = PetProvider.getRandomPet();

        addPet(petToAdd);
    }

    fun addPetFromView(name: String, description: String, age: String, giverId: String){
        val petToAdd = PetModel(name, description, age, giverId)

        addPet(petToAdd)
    }

    private fun addPet(pet: PetModel) {
        val currentPets: ArrayList<PetModel> = petModel.value!!;
        currentPets.add(pet);

        petModel.postValue(currentPets);
    }

    fun getPets(): MutableLiveData<ArrayList<PetModel>> {
        return petModel;
    }
}