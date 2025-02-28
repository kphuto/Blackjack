# Blackjack
import java.util.Scanner;
import java.util.ArrayList;

public class blackjack {


    /**
     * Check if user input 'y' or 'n'. If not, make them do it again.
     * @return character representing user's decision ('y' or 'n')
     */
    public static char getKey() {
        String key;
        Scanner scnr = new Scanner(System.in);
        key = scnr.nextLine();
        key = key.toLowerCase();
        while(((key.charAt(0) != 'y') && (key.charAt(0) != 'n')) || (key.length() > 1)) { // If input key is not 'y' or 'n', keep looping.
            System.out.println("Wrong key, please press again");
            key = scnr.nextLine();
            key = key.toLowerCase();
        }
        return key.charAt(0);
    }


    /**
     * Deal two cards to player and dealer
     * @param playerHand - ArrayList of cards in player's hand
     * @param dealerHand - ArrayList of cards in dealer's hand
     * @param playDeck - Deck used to play
     */
    public static void deal(ArrayList<card> playerHand, ArrayList<card> dealerHand, deck playDeck) {
        playerHand.add(playDeck.getCard());
        dealerHand.add(playDeck.getCard());
        playerHand.add(playDeck.getCard());
        dealerHand.add(playDeck.getCard());
    }

    /**
     * Print dealer's cards and player's cards
     * @param playerHand - ArrayList of cards in player's hand
     * @param dealerHand - ArrayList of cards in dealer's hand
     */
    public static void showBoard(ArrayList<card> playerHand, ArrayList<card> dealerHand) {
        System.out.println("Dealer's cards:");
        System.out.println("Face down card");
        System.out.println(dealerHand.get(1));
        System.out.println("Player's cards:");
        System.out.println(playerHand.get(0));
        System.out.println(playerHand.get(1));
    }

    /**
     * Check if the current cards are blackjack
     * @param hand - ArrayList of cards in the given hand
     * @return boolean representing whether the given hand has blackjack or not
     */
    public static boolean isBlackjack(ArrayList<card> hand) {
        if(((hand.get(0).getPoint() == 1) && (hand.get(1).getPoint() == 10)) ||
                ((hand.get(1).getPoint() == 1) && (hand.get(0).getPoint() == 10))) { // Blackjack when there is 1 'A' and 1 card with worth 10 points (10, J, Q, K)
            return true;
        }
        return false;
    }

    /**
     * Calculate and return the total points from the given cards
     * @param playerHand - ArrayList of cards in the given hand
     * @return integer representing the total point based on the cards in the given hand
     */
    public static int getPoint(ArrayList<card> playerHand) {
        boolean aceCount = false;
        int total = 0; // Total point of the hand
        for(int i = 0; i < playerHand.size(); i++) { // Loop over the list and add points from each card to the total.
            if(playerHand.get(i).getPoint() == 1) {
                aceCount = true;
            }
            total += playerHand.get(i).getPoint();
        }
        // Check if we have 'A' and can count it as 11 or not
        if(aceCount) {
            if(total + 10 <= 21) {
                total += 10;
            }
        }
        return total;
    }

    /**
     * Determines the player's decision for their hand ('1' to draw, '2' to pass, '3' to double down, and '4' to split)
     * @param playerMoney - the current amount of money player has
     * @param bet - the amount of bet of the round
     * @param playerHand - ArrayList of cards in player's hand
     * @return character representing the player's decision
     */
    public static char getAction(int[] playerMoney, int[] bet, ArrayList<card> playerHand) {
        String key;
        Scanner scnr = new Scanner(System.in);
        key = scnr.nextLine();
        //Check whether the given action is valid or not
        while((key.length() > 1) || ((key.charAt(0) != '1') && (key.charAt(0) != '2') && (key.charAt(0) != '3') && (key.charAt(0) != '4')) || (((key.charAt(0) == '3') || (key.charAt(0) == '4')) && (playerMoney[0] < bet[0] * 2)) || ((key.charAt(0) == '4') && (playerHand.get(0).getPoint() != playerHand.get(1).getPoint()))) { // If input key is not 'y' or 'n', keep looping.
            if(key.charAt(0) == '3') {
                // Unable to double down
                System.out.println("You cannot double down. Please try again.");
            }
            else if(key.charAt(0) == '4') {
                // Unable to split
                System.out.println("You cannot split. Please try again.");
            }
            else {
                // Invalid input key
                System.out.println("Wrong key, please press again");
            }
            key = scnr.nextLine();
        }
        //return the character for the action
        return key.charAt(0);
    }

    /**
     * Allowing player to play their turn and check for special winning condition (blackjack or busted)
     * @param playerHand - ArrayList of cards in player's hand
     * @param playDeck - deck used to play
     * @param winner - winner of this round
     * @param playerMoney - current amount of money player has
     * @param bet - the amount of bet of the round
     * @param newHand - ArrayList of cards represent the extra hand if player decides to split
     * @param split - indicating whether player decides to split
     */
    public static void playerTurn(ArrayList<card> playerHand, deck playDeck, int[] winner, int[] playerMoney, int[] bet, ArrayList<card> newHand, boolean[] split) {
        char key;
        char action;
        System.out.println("Your turn:");
        //Check if player has blackjack
        if(isBlackjack(playerHand)) {
            System.out.println("Player has blackjack!");
            winner[0] = 1;
            return;
        }
        System.out.println("Your point right now: " + getPoint(playerHand)); // Print player's current point
        System.out.println("What do you want to do? ('1' to draw a card, '2' to pass, '3' to double down, and '4' to split if you can");
        action = getAction(playerMoney, bet, playerHand);
        // Keep looping while player still want to get another card
        if(action == '1') {
            // Drawing cards
            do {
                card newCard = new card();
                newCard = playDeck.getCard(); // Get a card from the deck
                System.out.println(newCard); // Print the new card
                playerHand.add(newCard); // Add the new card to playerHand
                System.out.println("Your point right now: " + getPoint(playerHand)); // Print the total point after adding another card
                // Check if player is busted or not
                if (getPoint(playerHand) > 21) {
                    System.out.println("Player is busted.");
                    winner[0] = 2;
                    return;
                }
                System.out.println("Do you want to get a card? ('y' to get/ 'n' to pass)"); // Ask if player want to get another card
                key = getKey();
            } while (key == 'y');
        }
        else if(action == '3') {
            // Double down
            bet[0] *= 2;
            card newCard = new card();
            newCard = playDeck.getCard(); // Get a card from the deck
            System.out.println(newCard); // Print the new card
            playerHand.add(newCard); // Add the new card to playerHand
            System.out.println("Your point right now: " + getPoint(playerHand)); // Print the total point after adding another card
            // Check if player is busted or not
            if (getPoint(playerHand) > 21) {
                System.out.println("Player is busted.");
                winner[0] = 2;
                return;
            }
        }
        else if(action == '4') {
            // Split
            newHand.clear(); // Clear the newHand ArrayList
            // Move the second card from the playerHand to newHand
            newHand.add(playerHand.get(1));
            playerHand.remove(1);
            card newCard = new card();
            split[0] = true; // Indicate the player chose to split

            // The first hand after split
            // Print the current cards and points in the first hand
            System.out.println("Your first hand");
            System.out.println(playerHand.get(0));
            newCard = playDeck.getCard();
            System.out.println(newCard);
            playerHand.add(newCard);
            System.out.println("Your first hand point right now: " + getPoint(playerHand));
            // Check if the first hand has blackjack
            if(isBlackjack(playerHand)) {
                System.out.println("Player's hand 1 has blackjack!");
                winner[0] = 1;
            }
            // If the first hand doesn't have blackjack
            if(isBlackjack(playerHand) == false) {
                // Ask if the player wants to get another card
                System.out.println("Do you want to get another card? ('y' to get/ 'n' to pass)");
                key = getKey();
                boolean checkBusted = false;
                while ((key == 'y') && (checkBusted == false)){
                    newCard = playDeck.getCard(); // Get a card from the deck
                    System.out.println(newCard); // Print the new card
                    playerHand.add(newCard); // Add the new card to playerHand
                    System.out.println("Your first point right now: " + getPoint(playerHand)); // Print the total point after adding another card
                    // Check if player is busted or not
                    if (getPoint(playerHand) > 21) {
                        System.out.println("Player's hand 1 is busted.");
                        winner[0] = 2;
                        checkBusted = true;
                    }
                    if(checkBusted == false) {
                        System.out.println("Do you want to get a card? ('y' to get/ 'n' to pass)"); // Ask if player want to get another card
                        key = getKey();
                    }
                }
            }

            // The second hand after split
            // Print the current cards and points in the second hand
            System.out.println("Your second hand");
            System.out.println(newHand.get(0));
            newCard = playDeck.getCard();
            System.out.println(newCard);
            newHand.add(newCard);
            System.out.println("Your second hand point right now: " + getPoint(newHand));
            // Check if the second hand has blackjack
            if(isBlackjack(newHand)) {
                System.out.println("Player's hand 2 has blackjack!");
                winner[1] = 1;
            }
            // If the second hand doesn't have blackjack
            if(isBlackjack(newHand) == false) {
                // Ask if the player wants to get another card
                System.out.println("Do you want to get another card? ('y' to get/ 'n' to pass)");
                key = getKey();
                while (key == 'y') {
                    newCard = playDeck.getCard(); // Get a card from the deck
                    System.out.println(newCard); // Print the new card
                    newHand.add(newCard); // Add the new card to playerHand
                    System.out.println("Your second hand point right now: " + getPoint(newHand)); // Print the total point after adding another card
                    // Check if player is busted or not
                    if (getPoint(newHand) > 21) {
                        System.out.println("Player's hand 2 is busted.");
                        winner[1] = 2;
                        return;
                    }
                    System.out.println("Do you want to get a card? ('y' to get/ 'n' to pass)"); // Ask if player want to get another card
                    key = getKey();
                }
            }
        }
        return;
    }

    /**
     * Allowing dealer to play and check for special winning condition (blackjack or busted), dealer has to draw until there is at least 17 points
     * @param dealerHand - ArrayList of cards in dealer's hand
     * @param playDeck - deck used to play
     * @param winner - winner of the round
     */
    public static void dealerTurn(ArrayList<card> dealerHand, deck playDeck, int[] winner) {
        // Print the cards in dealerHand and the total point
        System.out.println("Dealer's hand:");
        System.out.println(dealerHand.get(0));
        System.out.println(dealerHand.get(1));
        // Check if dealer has blackjack
        if(isBlackjack(dealerHand)) {
            System.out.println("Dealer has blackjack!");
            winner[0] = 2;
            winner[1] = 2;
            return;
        }
        System.out.println("Dealer's point: " + getPoint(dealerHand)); // Print dealer's current point.
        // If total point in dealer hand is get than 17, keep getting another card.
        while(getPoint(dealerHand) < 17) {
            System.out.println("Dealer's hand is less than 17, so dealer must get a card.");
            System.out.println("Dealer gets a card");
            card newCard = new card();
            newCard = playDeck.getCard(); // Get another card from the deck
            System.out.println(newCard); // Print the card just get
            dealerHand.add(newCard); // Add the new card to playerHand
            System.out.println("Dealer's point: " + getPoint(dealerHand)); // Print the total point after adding a card.
        }
        // Check if dealer is busted.
        if(getPoint(dealerHand) > 21) {
            winner[0] = 1;
            winner[1] = 1;
        }
        return;
    }

    /**
     * Check if the string is an integer or not
     * @param str - given string
     * @return boolean representing whether the given string is an integer or not
     */
    public static boolean checkInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        }
        catch (NumberFormatException ex) {
            return false;
        }
    }

    /**
     * Get the bet amount from player, and check if the bet is valid
     * @param money - Current amount of money owned by player
     * @return integer representing the betting amount for the round
     */
    public static int getBet(int money) {
        Scanner scnr = new Scanner(System.in);
        String bet = scnr.next();
        // Keep looping until the bet is in range (0, playerMoney]
        while((checkInt(bet) == false) || ((checkInt(bet)) && ((Integer.parseInt(bet) > money) || (Integer.parseInt(bet) <= 0)))) {
            System.out.println("Invalid bet. Bet again.");
            bet = scnr.next();
        }
        return Integer.parseInt(bet);
    }

    /**
     * Get the amount of money the player want to play
     * @return integer representing the amount of money the player wants to play
     */
    public static int getStartingMoney() {
        Scanner scnr = new Scanner(System.in);
        String amount = scnr.next();
        while((checkInt(amount) == false) || (checkInt(amount) && (Integer.parseInt(amount) <= 0))) {
            System.out.println("Invalid amount. Choose again.");
            amount = scnr.next();
        }
        return Integer.parseInt(amount);
    }

    /**
     * Add the betting amount of money to the winner and subtracting it from the loser.
     * @param winnerMoney - the amount of money owned by the given winner
     * @param loserMoney - the amount of money owned by the given loser
     * @param betAmount - the bet amount of the round
     */
    public static void getMoney(int[] winnerMoney, int[] loserMoney, int[] betAmount) {
        // Check if dealer has enough money to pay. If not, update betAmount with the rest of dealer's money
        if(betAmount[0] > loserMoney[0]) {
            System.out.println("You win more than dealer can pay, you will only get the rest of dealer's money.");
            betAmount[0] = loserMoney[0];
        }
        winnerMoney[0] += betAmount[0]; // Add the winning betAmount to winner.
        loserMoney[0] -= betAmount[0]; // Subtract the betAmount from loser.
    }


    public static void main(String[] args) {
        ArrayList<card> playerHand = new ArrayList<card>(); // List of cards in player's hand
        ArrayList<card> dealerHand = new ArrayList<card>(); // List of cards in dealer's hand
        ArrayList<card> newHand = new ArrayList<card>(); // List of cards in extra hand when player wants to split
        deck playDeck = new deck(); // The deck will be used in the game
        char key; // Input 'y' or 'n' from user
        int[] winner = {0, 0}; // Winner by special case (blackjack or another player is busted)
        int[] playerMoney = {0}; // Player's money
        int[] dealerMoney = {0}; // Dealer's money
        int[] bet = {0}; // Player's bet money
        boolean[] split = {false};
        Scanner scnr = new Scanner(System.in);
        boolean iniAmount = false;

        System.out.println("Welcome to blackjack game");
        System.out.println("Do you want to play? ('y' to play/ 'n' to quit)");
        key = getKey();
        // Keep looping while player still want to play.
        while(key == 'y') {
            winner[0] = 0;
            winner[1] = 0;
            if(iniAmount == false) {
                System.out.println("Player please choose how much money you want to play.");
                int amount = getStartingMoney();
                iniAmount = true;
                playerMoney[0] = amount;
                dealerMoney[0] = amount;
            }
            // Print dealer's money and player's money
            System.out.println("Dealer current money: $" + dealerMoney[0]);
            System.out.println("Player current money: $" + playerMoney[0]);
            System.out.println("Player please bet. If you bet more than dealer can pay, you can only win the rest of dealer's money.");
            bet[0] = getBet(playerMoney[0]); // Get bet from player
            // Prepare for the game
            playDeck.shuffle();
            playerHand.clear();
            dealerHand.clear();
            deal(playerHand, dealerHand, playDeck); // Deal cards to player and dealer
            showBoard(playerHand, dealerHand); // Show the cards in dealerHand and playerHand
            // Player's turn to go
            playerTurn(playerHand, playDeck, winner, playerMoney, bet, newHand, split);
            // When player didn't split
            if(split[0] == false) {
                // Check if the player has blackjack or busted, if not then dealer will play
                if (winner[0] == 0) {
                    dealerTurn(dealerHand, playDeck, winner);
                }
                // If player wins by special case or player's point is greater than dealer's point
                if ((winner[0] == 1) || (winner[0] == 0) && (getPoint(playerHand) <= 21) && (getPoint(playerHand) > getPoint(dealerHand))) {
                    System.out.println("Player wins!");
                    // If player has blackjack, they win 3/2 of their bet
                    if (isBlackjack(playerHand)) {
                        bet[0] = bet[0] + (bet[0] / 2); // Update new bet = 3/2 bet
                    }
                    getMoney(playerMoney, dealerMoney, bet); // Add winning bet to player and subtract bet from dealer
                } // If dealer win by special case or dealer's point is greater than player's point
                else if ((winner[0] == 2) || ((winner[0] == 0) && (getPoint(dealerHand) <= 21) && (getPoint(dealerHand) > getPoint(playerHand)))) {
                    System.out.println("Dealer wins!");
                    getMoney(dealerMoney, playerMoney, bet); // Add bet to dealer and subtract losing bet from player
                } // When player and dealer have the same point, it's draw.
                else {
                    System.out.println("Draw!");
                }
            }
            // When player split
            else {
                boolean[] checkedHand = {false, false};
                // Check if hand 1 is busted
                if(winner[0] == 2) {
                    getMoney(dealerMoney, playerMoney, bet);
                    checkedHand[0] = true;
                }
                // Check if hand 1 has blackjack
                else if(winner[0] == 1) {
                    System.out.println("Your hand 1 wins.");
                    int temp = bet[0];
                    bet[0] += bet[0] / 2;
                    getMoney(playerMoney, dealerMoney, bet);
                    checkedHand[0] = true;
                    bet[0] = temp;
                }
                // Check if hand 2 is busted
                if(winner[1] == 2) {
                    getMoney(dealerMoney, playerMoney, bet);
                    checkedHand[1] = true;
                }
                // Check if hand 2 has blackjack
                else if(winner[1] == 1) {
                    int temp = bet[0];
                    bet[0] += bet[0] / 2;
                    getMoney(playerMoney, dealerMoney, bet);
                    checkedHand[1] = true;
                    bet[0] = temp;
                }
                // If at least one of the hands neither has blackjack nor busted, dealer's turn
                if((winner[0] == 0) || (winner[1] == 0)) {
                    dealerTurn(dealerHand, playDeck, winner);
                    // Compare the dealer's hand with the player's hand that is not determined the winner
                    for(int i = 0; i <= 1; i++) {
                        // If hand 1 is not determined
                        if(checkedHand[i] == false) {
                            if(winner[i] == 1) {
                                System.out.println("Your hand " + (i + 1) + " wins.");
                                getMoney(playerMoney, dealerMoney,bet);
                            }
                            else if(winner[i] == 2) {
                                System.out.println("Your hand " + (i + 1) + " loses.");
                                getMoney(dealerMoney, playerMoney, bet);
                            }
                            else if(i == 0) {
                                // If hand 1 has more points than the dealer
                                if(getPoint(playerHand) > getPoint(dealerHand)) {
                                    System.out.println("Your hand 1 wins.");
                                    getMoney(playerMoney, dealerMoney, bet);
                                }
                                // If dealer has more points than hand 1
                                else if(getPoint(dealerHand) > getPoint(playerHand)) {
                                    System.out.println("Your hand 1 loses.");
                                    getMoney(dealerMoney, playerMoney, bet);
                                }
                                // If hand 1 and dealer have the same point
                                else {
                                    System.out.println("Your hand 1 is draw");
                                }
                            }
                            // If hand 2 is not determined
                            else if(i == 1) {
                                // If hand 2 has more points than the dealer
                                if(getPoint(newHand) > getPoint(dealerHand)) {
                                    System.out.println("Your hand 2 wins.");
                                    getMoney(playerMoney, dealerMoney, bet);
                                }
                                // If dealer has more points than hand 2
                                else if(getPoint(dealerHand) > getPoint(newHand)) {
                                    System.out.println("Your hand 2 loses.");
                                    getMoney(dealerMoney, playerMoney, bet);
                                }
                                // If hand 2 and dealer have the same point
                                else {
                                    System.out.println("Your hand 2 is draw");
                                }
                            }
                        }
                    }
                }
            }
            // If dealer or player is out of money, the game is done. Exit the loop.
            if(playerMoney[0] <= 0) {
                System.out.println("Player is out of money. Game over!");
                break;
            }
            else if(dealerMoney[0] <= 0) {
                System.out.println("Dealer is out of money. Game over!");
                break;
            }
            // Ask if the player want to keep playing.
            System.out.println("Do you want to keep playing? ('y' to play/ 'n' to quit)");
            key = getKey();
            split[0] = false;
        }
        System.out.println("Your money after playing is: $" + playerMoney[0]); // Print player's money after playing.
    }
}
