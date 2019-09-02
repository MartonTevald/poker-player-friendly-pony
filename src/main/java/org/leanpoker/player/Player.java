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
        int current_buy_in = json.get("current_buy_in").getAsInt();
        JsonArray players = json.get("players").getAsJsonArray();
        int in_action = json.get("in_action").getAsInt();

        JsonObject myPlayer = players.get(in_action).getAsJsonObject();
        int bet = myPlayer.get("bet").getAsInt();


        return current_buy_in - bet;


    }

    public static void showdown(JsonElement game) {
    }
}
