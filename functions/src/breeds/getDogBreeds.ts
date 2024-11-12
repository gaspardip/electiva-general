import * as functions from "firebase-functions";

const dogBreeds = [
  "Otro", "Labrador Retriever", "Golden Retriever", "Bulldog", "Pastor Alemán", "Beagle",
  "Caniche (Poodle)", "Chihuahua", "Rottweiler", "Yorkshire Terrier", "Boxer", "Dálmata",
  "Shih Tzu", "Dóberman", "Border Collie", "Pug", "Mastín", "Schnauzer", "Cocker Spaniel",
  "Akita", "Husky Siberiano", "Gran Danés", "Bulldog Francés", "Maltés", "Shetland Sheepdog",
  "Boston Terrier", "Samoyedo", "Staffordshire Bull Terrier", "Corgi Galés", "Pointer",
  "Setter Irlandés", "Vizsla", "Basset Hound", "Bloodhound", "San Bernardo", "Saluki",
  "Terrier Escocés", "Airedale Terrier", "Weimaraner", "Collie", "Lhasa Apso", "Whippet",
  "Fox Terrier", "Chow Chow", "Basenji", "Pomerania", "Shiba Inu", "Galgo Italiano",
  "Boyero de Berna", "Keeshond", "Alaskan Malamute", "Papillon", "Leonberger", "Bichón Frisé",
  "Tosa Inu", "Dogo Argentino", "Cane Corso", "Perro Lobo Checoslovaco", "Galgo Español",
  "Perro de Agua Portugués", "Springer Spaniel", "Retriever de la Bahía de Chesapeake",
  "Retriever de Nueva Escocia", "Setter Gordon", "Schipperke", "Kerry Blue Terrier", "Bulmastín",
  "Pastor Ganadero Australiano", "Pastor Australiano", "Cocker Americano", "American Staffordshire Terrier",
  "Foxhound Inglés", "Perro Esquimal Americano", "Lakeland Terrier", "Jack Russell Terrier",
  "Rat Terrier", "Sealyham Terrier", "Manchester Terrier", "Silky Terrier", "Pekinés",
  "Perro Faraón", "Petit Basset Griffon Vendéen", "Coonhound", "Fila Brasileño", "Perro de Agua Español",
  "Spitz Alemán", "Borzoi", "Galgo", "Lebrel Afgano", "Otterhound", "Skye Terrier", "Lebrel Irlandés",
];

export const getDogBreeds = functions.https.onRequest((req, res) => {
  res.set("Access-Control-Allow-Origin", "*");
  res.status(200).json({ data: dogBreeds });
});
