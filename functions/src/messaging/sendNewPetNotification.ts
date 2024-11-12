import * as admin from "firebase-admin";
import { onDocumentCreated } from "firebase-functions/v2/firestore";

admin.initializeApp();

export const sendNewPetNotification = onDocumentCreated("pets/{petId}", (event) => {
  const newPet = event.data?.data();
  const title = "¡Nueva mascota disponible!";
  const body = `Una nueva mascota llamada ${newPet?.name} está disponible para adopción. ¡Dale un hogar!`;

  const message = {
    notification: {
      title: title,
      body: body,
    },
    topic: "pets_for_adoption",
  };

  return admin.messaging().send(message)
    .then((response) => {
      console.log("Message sent:", response);
    })
    .catch((error) => {
      console.log("Error sending message:", error);
    });
});
