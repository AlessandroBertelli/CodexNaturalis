package polimi.ingsw.json.readers;

import com.google.gson.*;

import java.io.FileReader;
import java.io.IOException;

//Requests format: [#plant, #animal, #insect, #fungi]

public class readDeckGold {
    public static void main(String[] args) {
        String path_to_jsonfile = "src/main/java/polimi/ingsw/json/decks/DeckGold.json";

        try{
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            JsonParser parser = new JsonParser();

            JsonElement jsonElement = parser.parse(new FileReader(path_to_jsonfile));

            JsonObject rootObject = jsonElement.getAsJsonObject();

            JsonArray deckGold = rootObject.getAsJsonArray("DeckGold");

            for (JsonElement element : deckGold) {
                JsonObject resourceObject = element.getAsJsonObject();

                int ID = resourceObject.get("ID").getAsInt();
                JsonArray frontCorner = resourceObject.getAsJsonArray("frontCorner");
                JsonArray backCorner = resourceObject.getAsJsonArray("backCorner");
                String imagePathToFront = resourceObject.get("imagePathToFront").getAsString();
                String imagePathToBack = resourceObject.get("imagePathToBack").getAsString();
                String attachedResource = resourceObject.get("attachedResource").getAsString();
                int point = resourceObject.get("point").getAsInt();
                String forEach = resourceObject.get("forEach").getAsString();
                JsonArray requests = resourceObject.getAsJsonArray("requests");

                System.out.println("ID: " + ID);
                System.out.println("Front Corner: " + frontCorner);
                System.out.println("Back Corner: " + backCorner);
                System.out.println("Image Path to Front: " + imagePathToFront);
                System.out.println("Image Path to Back: " + imagePathToBack);
                System.out.println("Attached Resource: " + attachedResource);
                System.out.println(point + " point(s) for each " + forEach);
                System.out.println("Placement request-> must have [4]: " +requests+ " on manuscript.");
                //System.out.println(point + " requests length: " + requests);
                System.out.println();
            }
        } catch (IOException e) {
            System.out.println("IOException in readDeckGold.");
        }
    }
}