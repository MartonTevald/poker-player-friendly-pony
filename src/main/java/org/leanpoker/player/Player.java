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
        JsonArray holeCards = json.get("hole_cards").getAsJsonArray();
        int currentBuyIn = json.get("current_buy_in").getAsInt();
        JsonArray players = json.get("players").getAsJsonArray();

        JsonElement currentPlayer = players.get(1);
        int inAction = json.get("in_action").getAsInt();

        for (int i = 0; i < players.size(); i++) {
            if (i == inAction){
                currentPlayer = players.get(i);
            }
        }

        int bet = currentBuyIn - (currentPlayer.getAsInt("bet"));


//        json.getAsJsonArray("players").get(inAction).getAsInt("bet");
//        current_buy_in - players[in_action][bet];


        return 0;
    }

    public static void showdown(JsonElement game) {
    }
}
