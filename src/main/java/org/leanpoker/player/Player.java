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

        int bet = json.get("bet").getAsInt();



        current_buy_in - players[in_action][bet];


        return 0;
    }

    public static void showdown(JsonElement game) {
    }
}
