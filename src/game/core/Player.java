package game.core;


import java.util.ArrayList;

/*
* klasa gracza
* */
/**
 * @author Paulina Staniszewska-Tudruj
 *
 */
public class Player {
    private String playerName; // nazwa gracza
    private ArrayList<Card> deck = new ArrayList<>(); // karty na rêku gracza

    // konstruktor bezparametrowy
    public Player(){
        this("unknown");
    }

    // konstruktor z imieniem
    public Player(String name){
        playerName = name;
    }

    // dodanie karty
    public void addCard(Card cardFromStack){
        deck.add(cardFromStack);
    }

    // dodanie serii kart
    public void addCards(ArrayList<Card> cardsFromStack){
        deck.addAll(cardsFromStack);
    }

    // pobranie nazwy gracza
    public String getPlayerName() {
        return playerName;
    }

    // pobranie talii gracza
    public ArrayList<Card> getDeck() {
        return deck;
    }

    // wypisanie talii gracza
    public void printPlayersDeck(){
        System.out.println("\n-----------------");
        System.out.println("Player: " + getPlayerName());
        System.out.println("id; name; color");
        for(Card next : deck){
            System.out.print(deck.indexOf(next) + ": " + next.getCardName());
            if(next.hasColor()) System.out.print("\t\t " + next.getColorName());
            System.out.println();
        }
        System.out.println("-----------------\n");
    }

    // usuniêcie karty
    public void removeCard(Integer cardId){
        deck.remove(deck.get(cardId));
    }
}
