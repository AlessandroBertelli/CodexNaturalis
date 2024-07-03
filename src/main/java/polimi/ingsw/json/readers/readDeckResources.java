package polimi.ingsw.json.readers;

//import com.google.gson.*;
import com.google.gson.*;

import java.io.FileReader;
import java.io.IOException;

/*
Test 0 - Cards & json
Free -> No item on available corner.
null -> No available corner at all.
Element0 -> top left corner, element1 -> top right corner, element2 -> bottom right corner, element3 -> bottom left corner.
*/

public class readDeckResources {
    public static void main(String[] args) {
        String path_to_jsonfile = "src/main/java/polimi/ingsw/json/decks/DeckResources.json";

        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            JsonParser parser = new JsonParser();

            JsonElement jsonElement = parser.parse(new FileReader(path_to_jsonfile));

            JsonObject rootObject = jsonElement.getAsJsonObject();

            JsonArray deckResources = rootObject.getAsJsonArray("DeckResources");

            for (JsonElement element : deckResources) {
                JsonObject resourceObject = element.getAsJsonObject();

                int ID = resourceObject.get("ID").getAsInt();
                JsonArray frontCorner = resourceObject.getAsJsonArray("frontCorner");
                JsonArray backCorner = resourceObject.getAsJsonArray("backCorner");
                String imagePathToFront = resourceObject.get("imagePathToFront").getAsString();
                String imagePathToBack = resourceObject.get("imagePathToBack").getAsString();
                String attachedResource = resourceObject.get("attachedResource").getAsString();
                int point = resourceObject.get("point").getAsInt();

                System.out.println("ID: " + ID);
                System.out.println("Front Corner: " + frontCorner);
                System.out.println("Back Corner: " + backCorner);
                System.out.println("Image Path to Front: " + imagePathToFront);
                System.out.println("Image Path to Back: " + imagePathToBack);
                System.out.println("Attached Resource: " + attachedResource);
                System.out.println("Point: " + point);
                System.out.println();
            }
        } catch (IOException e) {
            System.out.println("IOException in readDeckResources.");
        }
    }
}