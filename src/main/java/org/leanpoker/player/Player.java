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
        int minimum_raise = json.get("minimum_raise").getAsInt();
        JsonObject myPlayer = players.get(in_action).getAsJsonObject();
        int ourBet = myPlayer.get("bet").getAsInt();

        // This is the call method
        int check = current_buy_in - ourBet;
        int raise = current_buy_in - ourBet + minimum_raise;

        return check;
    }

    public static void showdown(JsonElement game) {
    }
}
