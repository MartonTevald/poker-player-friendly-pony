package org.leanpoker.player;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class Player {

    static final String VERSION = "Gathering data with rainbows";

    private static List<Integer> getCardRanks(JsonArray cards) {
        List<Integer> ranks = new ArrayList<>();

        for (JsonElement card : cards) {
            JsonObject c = card.getAsJsonObject();
            String currRank = c.get("rank").toString();
            ranks.add(currRank.equals("J") ?
                    11 : currRank.equals("Q") ?
                    12 : currRank.equals("K") ?
                    13 : currRank.equals("A") ?
                    14 : c.get("rank").getAsInt());
        }

        return ranks;
    }

    private static List<String> getCardSuits(JsonArray cards) {
        List<String> suits = new ArrayList<>();

        for (JsonElement card : cards) {
            JsonObject c = card.getAsJsonObject();
            suits.add(c.get("suit").getAsString());
        }

        return suits;
    }


    private static int bestMatch(JsonArray myCards, JsonArray communityCards) {
        List<String> suits;
        List<Integer> ranks;
        ranks = getCardRanks(myCards);
        ranks.addAll(getCardRanks(communityCards));

        suits = getCardSuits(myCards);
        suits.addAll(getCardSuits(communityCards));

        return 0;
    }

    public static int betRequest(JsonElement request) {
        // Obtaining the JSON file
        JsonObject json = request.getAsJsonObject();

        // Obtaining player information
        JsonArray players = json.get("players").getAsJsonArray();

        int in_action = json.get("in_action").getAsInt();
        JsonObject myPlayer = players.get(in_action).getAsJsonObject();

        // Getting the roles [dealer, small blind, big blind
        int small_blind = json.get("small_blind").getAsInt();
        int big_blind = small_blind * 2;
        int dealer = json.get("dealer").getAsInt();


        // Getting info on chips
        int pot = json.get("pot").getAsInt();
        int current_buy_in = json.get("current_buy_in").getAsInt();
        int minimum_raise = json.get("minimum_raise").getAsInt();

        // Get card info
        JsonArray communityCards = json.get("community_cards").getAsJsonArray();
        JsonArray in_hand_cards = myPlayer.get("hole_cards").getAsJsonArray();

        // Getting round info
        int bet_round = json.get("bet_index").getAsInt();
        int ourChips = myPlayer.get("stack").getAsInt();
        int ourBet = myPlayer.get("bet").getAsInt();
        int highestBet = 0;

        for (JsonElement player : players) {
            if (player.getAsJsonObject().get("bet").getAsInt() > highestBet) {
                highestBet = player.getAsJsonObject().get("bet").getAsInt();
            }
        }

        // This is the call method
        int check = current_buy_in - ourBet;
        int raise = current_buy_in - ourBet + minimum_raise;

        if (checkForPairsInHand(in_hand_cards, bet_round)) {
            return raise + (pot / 100) * 10;
        }

        return check;
    }

    public static void showdown(JsonElement game) {
    }

    public static boolean checkForPairsInHand(JsonArray in_hand_cards, int bet_round) {
        int handCard1 = in_hand_cards.get(0).getAsJsonObject().get("rank").getAsInt();
        int handCard2 = in_hand_cards.get(1).getAsJsonObject().get("rank").getAsInt();

        return (handCard1 == handCard2 && bet_round == 0);
    }

}


