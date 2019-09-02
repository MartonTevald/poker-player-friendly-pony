package org.leanpoker.player;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

public class Player {

    static final String VERSION = "Java 0.1";

    public static int betRequest(JsonElement request) {
        JsonObject json = request.getAsJsonObject();
        JsonArray communityCards = json.get("community_cards").getAsJsonArray();


        return 0;
    }

    public static void showdown(JsonElement game) {
    }
}
