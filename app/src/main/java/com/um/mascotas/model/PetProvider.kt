package com.um.mascotas.model

class PetProvider {
    companion object {
        val pets = listOf<PetModel>(
            PetModel("Bella", "Labrador Retriever, friendly and energetic", "3 years", "giver123"),
            PetModel("Max", "German Shepherd, loyal and protective", "5 years", "giver124"),
            PetModel("Charlie", "Beagle, curious and playful", "2 years", "giver125"),
            PetModel("Lucy", "Bulldog, calm and affectionate", "4 years", "giver126"),
            PetModel("Molly", "Poodle, intelligent and friendly", "1 year", "giver127"),
            PetModel("Buddy", "Golden Retriever, gentle and loving", "6 years", "giver128"),
            PetModel("Daisy", "Boxer, energetic and playful", "3 years", "giver129"),
            PetModel("Lola", "Siberian Husky, active and friendly", "2 years", "giver130"),
            PetModel("Sophie", "Shih Tzu, affectionate and alert", "4 years", "giver131"),
            PetModel("Chloe", "Cocker Spaniel, gentle and loving", "5 years", "giver132")
        );

        fun getRandomPet(): PetModel {
            val positionToReturn: Int = (0..9).random();

            return pets[positionToReturn];
        }
    }
}