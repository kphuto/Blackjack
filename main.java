import java.util.*;

class Card {
    private String suit;
    private String rank;
    private int value;

    public Card(String suit, String rank, int value) {
        this.suit = suit;
        this.rank = rank;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String toString() {
        return rank + " of " + suit;
    }
}

class Deck {
    private List<Card> cards;
    private Random random;

    public Deck() {
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};
        int[] values = {2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10, 11};
        
        cards = new ArrayList<>();
        random = new Random();
        
        for (int i = 0; i < suits.length; i++) {
            for (int j = 0; j < ranks.length; j++) {
                cards.add(new Card(suits[i], ranks[j], values[j]));
            }
        }
        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(cards, random);
    }

    public Card drawCard() {
        return cards.remove(0);
    }
}

public class Blackjack {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean playAgain = true;
        while (playAgain) {
            playBlackjack();
            System.out.print("Do you want to play again? (yes/no): ");
            String input;
            while (true) {
                input = scanner.next();
                if (input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("no")) {
                    break;
                }
                System.out.print("Invalid input. Please enter 'yes' or 'no': ");
            }
            playAgain = input.equalsIgnoreCase("yes");
        }
    }

    public static void playBlackjack() {
        Deck deck = new Deck();
        List<Card> playerHand = new ArrayList<>();
        List<Card> dealerHand = new ArrayList<>();
        
        playerHand.add(deck.drawCard());
        playerHand.add(deck.drawCard());
        dealerHand.add(deck.drawCard());
        dealerHand.add(deck.drawCard());
        
        System.out.println("Dealer's hand: " + dealerHand.get(0) + " and [Hidden]");
        System.out.println("Your hand: " + playerHand);
        
        while (getHandValue(playerHand) < 21) {
            System.out.print("Hit or Stand? (hit/stand): ");
            String move;
            while (true) {
                move = scanner.next();
                if (move.equalsIgnoreCase("hit") || move.equalsIgnoreCase("stand")) {
                    break;
                }
                System.out.print("Invalid input. Please enter 'hit' or 'stand': ");
            }
            if (move.equalsIgnoreCase("hit")) {
                playerHand.add(deck.drawCard());
                System.out.println("Your hand: " + playerHand);
            } else {
                break;
            }
        }
        
        int playerTotal = getHandValue(playerHand);
        if (playerTotal > 21) {
            System.out.println("Bust! You lost.");
            return;
        }
        
        System.out.println("Dealer's hand: " + dealerHand);
        while (getHandValue(dealerHand) < 17) {
            dealerHand.add(deck.drawCard());
            System.out.println("Dealer draws: " + dealerHand.get(dealerHand.size() - 1));
        }
        
        int dealerTotal = getHandValue(dealerHand);
        System.out.println("Dealer's total: " + dealerTotal);
        System.out.println("Your total: " + playerTotal);
        
        if (dealerTotal > 21 || playerTotal > dealerTotal) {
            System.out.println("You win!");
        } else if (playerTotal == dealerTotal) {
            System.out.println("It's a tie!");
        } else {
            System.out.println("Dealer wins!");
        }
    }
    
    private static int getHandValue(List<Card> hand) {
        int total = 0;
        int aceCount = 0;
        for (Card card : hand) {
            total += card.getValue();
            if (card.getValue() == 11) {
                aceCount++;
            }
        }
        
        while (total > 21 && aceCount > 0) {
            total -= 10;
            aceCount--;
        }
        return total;
    }
}
