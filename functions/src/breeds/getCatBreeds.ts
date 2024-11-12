import * as functions from "firebase-functions";

const catBreeds = [
  "Otro", "Persa", "Siamés", "Maine Coon", "Bengala", "Sphynx", "Birmano", "Británico de Pelo Corto",
  "Ragdoll", "Somalí", "Abisinio", "Scottish Fold", "Americano de Pelo Corto", "Exótico de Pelo Corto",
  "Oriental", "Cartujo", "Manx", "Ocicat", "Tonquinés", "Himalayo", "Balinés", "Bobtail Japonés",
  "Devon Rex", "Cornish Rex", "Burmés", "Bombay", "Sagrado de Birmania", "Azul Ruso", "Savannah",
  "Bosque de Noruega", "Angora Turco", "Selkirk Rex", "Korat", "Singapura", "Van Turco", "LaPerm",
  "Nebelung", "Peterbald", "Pixie Bob", "Burmilla", "Toyger", "Curl Americano", "Foldex",
  "Munchkin", "Cymric", "Snowshoe", "Chausie", "Europeo de Pelo Corto", "Americano de Pelo Duro",
  "Brazilian Shorthair", "Khao Manee", "Lykoi", "Ojos Azules", "Siberiano", "Tiffanie",
];

export const getCatBreeds = functions.https.onRequest((req, res) => {
  res.set("Access-Control-Allow-Origin", "*");
  res.status(200).json({ data: catBreeds });
});
