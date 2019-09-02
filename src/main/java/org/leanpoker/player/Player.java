package org.leanpoker.player;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
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

    private static boolean basicStraightCheck(int starter, List<Integer> ranks) {
        Collections.sort(ranks);
        int straightCounter = 0;
        for (int i=starter ; i < ranks.size(); i++) {
            if (ranks.get(i) == ranks.get(i-1) + 1){
                straightCounter++;
            }
            else {
                straightCounter = 0;
            }
        }
        return (straightCounter > 4);
    }


    private static boolean basicFlushCheck(List<String> suits) {
        int flush = 0;
        for (String basicSuit : suits) {
            for (String otherSuit : suits) {
                if (basicSuit.equals(otherSuit)) {
                    flush++;
                }
                if (flush > 5) {
                    break;
                }
            }
        }
        return (flush > 5);
    }

    private static boolean FlushCheck(int starter, List<Integer> ranks, List<String> suits) {
        boolean straightOrNot = basicStraightCheck(starter, ranks);
        boolean flushOrNot = basicFlushCheck(suits);


        return (straightOrNot && flushOrNot);

    }

    private static boolean checkBestCaseScenario(JsonArray myCards, JsonArray communityCards){
        List<String> suits;
        List<Integer> ranks;
        ranks = getCardRanks(myCards);
        ranks.addAll(getCardRanks(communityCards));

        suits = getCardSuits(myCards);
        suits.addAll(getCardSuits(communityCards));

        //Royal Flush
        boolean isRoyalFlush = FlushCheck(9, ranks, suits);
        //Straight Flush
        boolean isStraightFlush = FlushCheck(1, ranks, suits);
        //Flush
        boolean isFlush = basicFlushCheck(suits);
        //Straight
        boolean isStraight = basicStraightCheck(1, ranks);

        if( isRoyalFlush || isStraightFlush || isFlush || isStraight) {
            return true;
        }
        return false;
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

        //if (checkBestCaseScenario(in_hand_cards, communityCards)) return 1000;

        if (checkForTwoPairs(in_hand_cards, communityCards)){
            return raise + 300;
        }
        if (checkForPairs(in_hand_cards, communityCards, bet_round)) {
            return raise + 150;
        }

        if(raise > ourChips/2){
            return 0;
        }


        return check;
    }

    static void showdown(JsonElement game) {
    }

    private static boolean checkForPairs(JsonArray in_hand_cards, JsonArray community_cards, int bet_round) {
        String handCard1 = in_hand_cards.get(0).getAsJsonObject().get("rank").getAsString();
        String handCard2 = in_hand_cards.get(1).getAsJsonObject().get("rank").getAsString();

        if (bet_round == 0 && handCard1.equals(handCard2)) {
            return true;
        }

        for (JsonElement card : community_cards) {
            String cardRank = card.getAsJsonObject().get("rank").getAsString();


            if (cardRank.equals(handCard1) || cardRank.equals(handCard2)) {
                return true;
            }
        }

        return false;
    }

    private static boolean checkForTwoPairs(JsonArray in_hand_cards, JsonArray community_cards) {
        String handCard1 = in_hand_cards.get(0).getAsJsonObject().get("rank").getAsString();
        String handCard2 = in_hand_cards.get(1).getAsJsonObject().get("rank").getAsString();
        int foundPairs = 0;

        for (JsonElement card : community_cards) {
            String cardRank = card.getAsJsonObject().get("rank").getAsString();

            if (cardRank.equals(handCard1) || cardRank.equals(handCard2)) {
                foundPairs++;
            }
        }

        return foundPairs >= 2;
    }
}


