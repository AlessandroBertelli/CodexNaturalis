package polimi.ingsw.json.readers;

import com.google.gson.*;
import java.io.FileReader;
import java.io.IOException;

//Attached resource format: [#plant, #animal, #insect, #fungi, #quill, #inkwell, #manuscript]

public class readDeckTarget {
    public static void main(String[] args) {
        String path_to_jsonfile = "src/main/java/polimi/ingsw/json/decks/DeckTarget.json";

        try{
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            JsonParser parser = new JsonParser();

            JsonElement jsonElement = parser.parse(new FileReader(path_to_jsonfile));

            JsonObject rootObject = jsonElement.getAsJsonObject();

            JsonArray deckTarget = rootObject.getAsJsonArray("DeckTarget");

            for (JsonElement element : deckTarget) {
                JsonObject resourceObject = element.getAsJsonObject();

                int ID = resourceObject.get("ID").getAsInt();
                String imagePathToFront = resourceObject.get("imagePathToFront").getAsString();
                String imagePathToBack = resourceObject.get("imagePathToBack").getAsString();
                String color = resourceObject.get("color").getAsString();
                int point = resourceObject.get("point").getAsInt();
                String strategy = resourceObject.get("strategy").getAsString();

                System.out.println("ID: " + ID);
                System.out.println("Color: " + color);
                System.out.println("Image Path to Front: " + imagePathToFront);
                System.out.println("Image Path to Back: " + imagePathToBack);
                System.out.println(point + " point(s) for each " + strategy);
                System.out.println();
            }
        } catch (IOException e) {
            System.out.println("IOException in readDeckTarget.");
        }
    }
}
