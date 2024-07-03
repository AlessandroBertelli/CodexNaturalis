package polimi.ingsw.json.readers;

import com.google.gson.*;
import java.io.FileReader;
import java.io.IOException;

// numResources might be extra
//Attached resource format: [#plant, #animal, #insect, #fungi]

public class readDeckStarting {
    public static void main(String[] args) {
        String path_to_jsonfile = "src/main/java/polimi/ingsw/json/decks/DeckStarting.json";

        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            JsonParser parser = new JsonParser();

            JsonElement jsonElement = parser.parse(new FileReader(path_to_jsonfile));

            JsonObject rootObject = jsonElement.getAsJsonObject();

            JsonArray deckStarting = rootObject.getAsJsonArray("DeckStarting");

            for (JsonElement element : deckStarting) {
                JsonObject resourceObject = element.getAsJsonObject();

                int ID = resourceObject.get("ID").getAsInt();
                JsonArray frontCorner = resourceObject.getAsJsonArray("frontCorner");
                JsonArray backCorner = resourceObject.getAsJsonArray("backCorner");
                String imagePathToFront = resourceObject.get("imagePathToFront").getAsString();
                String imagePathToBack = resourceObject.get("imagePathToBack").getAsString();
                JsonArray attachedResource = resourceObject.getAsJsonArray("attachedResource");

                System.out.println("ID: " + ID);
                System.out.println("Front Corner: " + frontCorner);
                System.out.println("Back Corner: " + backCorner);
                System.out.println("Image Path to Front: " + imagePathToFront);
                System.out.println("Image Path to Back: " + imagePathToBack);
                System.out.println("Attached Resource(s): " + attachedResource);
                System.out.println();
            }
        } catch (IOException e) {
            System.out.println("IOException in readDeckStarting.");
        }
    }
}