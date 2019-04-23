package game.core;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Paulina Staniszewska-Tudruj
 *
 */
public class Game {

	/**
	 * Utworzenie podstawowej talii do gry
	 * */
    private static ArrayList<Card> createStack(){
        ArrayList<Card> deck = new ArrayList<>();

        for(int k = 0; k < 2; k++) {
            for (int i = k; i <= 12; i++) {
                deck.add(new Card(i, Card.ColorRed));
                deck.add(new Card(i, Card.ColorGreen));
                deck.add(new Card(i, Card.ColorBlue));
                deck.add(new Card(i, Card.ColorYellow));
            }
        }

        return deck;
    }

    private ArrayList<Card> stack; // stos kart
    private ArrayList<Player> players; // lista graczy
    private Integer playerCount; // liczba graczy
    private Integer actualPlayer; // aktualny gracz
    private Integer directory; // kierunek gry
    private final static Integer StartingCards = 5; // liczba kart na start dla gracza
    private final static Integer MinPlayers = 2; // minimalna liczba graczy
    private final static Integer MaxPlayers = 2; // maksymalna liczba graczy

    private Integer actualNumber; // aktualny numer karty
    private Integer actualColor; // aktualny numer koloru
    private Integer cardsToDraw = 0; // liczba kart do pobrania przez nastepnego gracza

	// wejœcia / wyjœcia
	private BufferedReader[] inputFromP;
	private BufferedReader[] inputFromPGate;
	private BufferedReader[] inputFromPRespond;

	private PrintWriter[] outputToP;
	private PrintWriter[] outputToPGate;
	private PrintWriter[] outputToPRespond;

	// pobranie karty ze stosu
    private Card takeFromStack(){
        Card toReturn = stack.get(0);
        stack.remove(0);
        return toReturn;
    }

    // dodanie karty na stos
    private void addToStack(Card cardToAdd) {
        stack.add(cardToAdd);
    }

    // konstruktor gry przyjmujacy wejœcia/wyjœcia
    public Game(BufferedReader[] inP, PrintWriter[] outP, BufferedReader[] inPG, PrintWriter[] outPG, BufferedReader[] inPR, PrintWriter[] outPR) throws Exception {
        
    	inputFromP = inP;
    	outputToP = outP;
    	inputFromPGate = inPG;
    	outputToPGate = outPG;
    	inputFromPRespond = inPR;
    	outputToPRespond = outPR;

    	// utworzenie tali do gry
    	stack = createStack();
        players = new ArrayList<>();
        Collections.shuffle(stack);

        printInitMessage();

        playerCount = 2;
        actualPlayer = 0;
        directory = 1;

        // sprawdzenie liczby graczy
        if( playerCount < MinPlayers || MaxPlayers < playerCount){
            throw new Exception("Bad player count!");
        }

        // pobranie nicków graczy
        for(int i = 0; i < playerCount; i++){
            System.out.print("Player number " + (i + 1) + " name: ");

            outputToP[i].println("NAME");
            String playerName = inputFromP[i].readLine();
            System.out.println(playerName);

            players.add(new Player(playerName));
        }

        // pobranie kart dla graczy
        for(int i = 0; i < StartingCards; i++){
            for(Player player : players){
            	Card c = takeFromStack();
                player.addCard(c);
                outputToP[players.indexOf(player)].println("ADD " + c.getNumber() + " " + c.getColor());
                outputToP[players.indexOf(player)].println("ADD_OPP");
            }
        }

        System.out.println("The game is ready!");
    }

    // wiadomoœæ powitalna
    private void printInitMessage() {
        System.out.println("Hello in UNO!");
        System.out.print("Players: ");
    }

    // ustawienie nastêpnego gracza
    private void nextPlayer(){
        actualPlayer = (actualPlayer + directory + playerCount) % playerCount;
    }

    // odwrócenie kierunku gry
    private void reverse(){
        if(directory == 1) directory = playerCount - 1;
        else directory = 1;
    }

    // rozpoczêcie gry
    public void play(){
    	
    	try {
    		// pobranie pocz¹tkowej karty
	        Card startingCard = takeFromStack();
	
	        while(!startingCard.isNormalCard()){
	            addToStack(startingCard);
	            startingCard = takeFromStack();
	        }
	        
	        System.out.println(startingCard);
	
	        actualColor = startingCard.getColor();
	        actualNumber = startingCard.getNumber();
	
	        outputToP[0].println("STARTING " + actualNumber + " " + actualColor);
	        outputToP[1].println("STARTING " + actualNumber + " " + actualColor);

	        // rozpoczêcie rozgrywki
	        while(true) {

	        	// dobranie kart
	            while(cardsToDraw --> 0){           //operator d¹¿¹cy
					addCardToActualPlayerFromStackAndReloadOpponent();
				}

	            // wyœwietlanie wiadomoœci
	            System.out.println("Player " + players.get(actualPlayer).getPlayerName() + " (" + (actualPlayer + 1) + ")" + " turn!");
	            if(actualNumber == null) System.out.println("Actual card: - " + Card.getColorName(actualColor));
	            else System.out.println("Actual card: " + Card.getCardName(actualNumber) + " " + Card.getColorName(actualColor));
	            System.out.println("What to do?\ntype in \"play card_number\" or \"draw\"");
	            players.get(actualPlayer).printPlayersDeck();

	            // przyjêcie polecenia od gracza (otworzenie po³¹czenia, przyjêcie komendy, zamkniêcie po³¹czenia)
	            outputToPGate[actualPlayer].println("OPEN");
	            String playersChoice = inputFromP[actualPlayer].readLine();
	            System.out.println(playersChoice);
				outputToPGate[actualPlayer].println("CLOSE");

				// wykonanie polecenia
	            if(playersChoice.equals("draw")){
	            	// dobranie karty
					addCardToActualPlayerFromStackAndReloadOpponent();
				} else if(playersChoice.startsWith("play")){
	            	// zagranie karty
	                int cardNumber = Integer.parseInt(playersChoice.split(" ")[1]);
	                Card chosenCard = players.get(actualPlayer).getDeck().get(cardNumber);
	                if(!canPlayCard(chosenCard)) {
	                	// odrzucenie karty
	                	outputToPRespond[actualPlayer].println("NOT");
	                    continue;
	                }

	                // przyjêcie karty
	                outputToPRespond[actualPlayer].println("OK");

	                // aktualizacja sto³u
	                outputToP[(actualPlayer + 1) % 2].println("STARTING " + chosenCard.getNumber() + " " + chosenCard.getColor());

	                // aktulizacja graczy
	                players.get(actualPlayer).removeCard(cardNumber);
	                outputToP[(actualPlayer + 1) % playerCount].println("REM_OPP");

	                if(players.get(actualPlayer).getDeck().size() == 1){
	                    System.out.println("\n\nUNO!\n\n");
	                }

	                // koniec rozgrywki
	                if(players.get(actualPlayer).getDeck().size() == 0){
	                	outputToP[actualPlayer].println("MESSAGE_END You WON!");
	                	outputToP[(actualPlayer + 1) % playerCount].println("MESSAGE_END " + players.get(actualPlayer).getPlayerName() + " WON!");
	                    System.out.println(players.get(actualPlayer).getPlayerName() + " WON!");
	                    break;
	                }

	                // powrót karty do stosu
	                addToStack(chosenCard);

	                // wykonanie akcji karty
	                if(chosenCard.getAction() == 5 || chosenCard.getAction() == 6){
	                	// jeszcze nie dzia³a
	                    System.out.println("Change color to: ");
	                    int id = 1;
	                    for(String color : Card.ColorNames){
	                        System.out.println((id++) + " " + color);
	                    }
	                    outputToP[actualPlayer].println("PICK");
	                    Integer newColor = Integer.parseInt(inputFromPRespond[actualPlayer].readLine());
	                    actualColor = Card.ColorBase + newColor;
	                    actualNumber = null;
	
	                    if(chosenCard.getAction() == 6){
	                        cardsToDraw = 4;
	                    }
	                } else {
	                    if (chosenCard.getAction() == 2) {
	                        // pominiêcie gracza
	                    	nextPlayer();
	                    } else if (chosenCard.getAction() == 3) {
	                    	// zmiana kierunku gry
	                        reverse();
	                    } else if (chosenCard.getAction() == 4) {
	                    	// dobranie kart przez gracza
	                        cardsToDraw = 2;
	                    }
	                    actualNumber = chosenCard.getNumber();
	                    actualColor = chosenCard.getColor();
	                }
	            } else {
	            	// b³êdna komenda
	                System.out.println("Bad command!");
	                continue;
	            }
	
	            nextPlayer();
	        }
        
    	} catch (Exception e) {
			e.printStackTrace();
		}
    }

    // dobranie karty przez aktualnego gracza i aktualizacja oponenta
	private void addCardToActualPlayerFromStackAndReloadOpponent() {
		Card c = takeFromStack();
		players.get(actualPlayer).addCard(c);
		outputToP[actualPlayer].println("ADD " + c.getNumber() + " " + c.getColor());
		outputToP[(actualPlayer + 1) % playerCount].println("ADD_OPP");
	}

	// sprawdzenie, czy mo¿na zagraæ kartê
	private boolean canPlayCard(Card card) {
        if(Card.NotColored.contains(card.getNumber())){
            return true;
        }
        if(actualNumber == null && card.getColor().equals(actualColor)){
            return true;
        }
        return card.getNumber().equals(actualNumber) || card.getColor().equals(actualColor);
    }
}
