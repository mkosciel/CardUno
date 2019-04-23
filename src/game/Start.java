package game;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import game.core.Game;

/**
 * @author Micha³ Koœcielak
 *
 */
public class Start {
	
	private static final Integer PORT = 8910;
	private static final Integer GatePORT = 8911;
	private static final Integer RespondPORT = 8912;

	private static final Integer TIME_OUT = 3600 * 1000;

	public static void main(String[] args) {
		startServer();
	}

	private static void startServer() {

		System.out.println("Starting!");

		try {
			/**
			  Uruchamianie socketów
			  */
			ServerSocket listener = new ServerSocket(PORT);
			ServerSocket listenerGate = new ServerSocket(GatePORT);
			ServerSocket listenerRespond = new ServerSocket(RespondPORT);

			// po³¹czeñ z u¿ytkownikami
			Socket[] player = {listener.accept(), listener.accept()}; player[0].setSoTimeout(TIME_OUT); player[1].setSoTimeout(TIME_OUT);
			Socket[] playerGate = {listenerGate.accept(), listenerGate.accept()}; playerGate[0].setSoTimeout(TIME_OUT); playerGate[1].setSoTimeout(TIME_OUT);
			Socket[] playerRespond = {listenerRespond.accept(), listenerRespond.accept()}; playerRespond[0].setSoTimeout(TIME_OUT); playerRespond[1].setSoTimeout(TIME_OUT);

			// strumienie wyjœæ u¿ytkowników
			BufferedReader[] inputFromP = {new BufferedReader(new InputStreamReader(player[0].getInputStream())), new BufferedReader(new InputStreamReader(player[1].getInputStream()))};
			BufferedReader[] inputFromPGate = {new BufferedReader(new InputStreamReader(playerGate[0].getInputStream())), new BufferedReader(new InputStreamReader(playerGate[1].getInputStream()))};
			BufferedReader[] inputFromPRespond = {new BufferedReader(new InputStreamReader(playerRespond[0].getInputStream())), new BufferedReader(new InputStreamReader(playerRespond[1].getInputStream()))};

			// strumienie wejœæ u¿ytkowników
			PrintWriter[] outputToP = {new PrintWriter(player[0].getOutputStream(), true), new PrintWriter(player[1].getOutputStream(), true)};
			PrintWriter[] outputToPGate = {new PrintWriter(playerGate[0].getOutputStream(), true), new PrintWriter(playerGate[1].getOutputStream(), true)};
			PrintWriter[] outputToPRespond = {new PrintWriter(playerRespond[0].getOutputStream(), true), new PrintWriter(playerRespond[1].getOutputStream(), true)};

			// utworzenie obiektu gry, przes³anie wejœæ/wyjœæ i rozpoczêcie
			Game game = new Game(inputFromP, outputToP, inputFromPGate, outputToPGate, inputFromPRespond, outputToPRespond);
			game.play();

		} catch (SocketException e) {
			System.out.println("Gra pad³a! :(");
			System.out.println(e.getMessage());
		}catch (Exception e) {
			System.out.println(e.getMessage());
		} 
		
		System.out.println("Ending...");
	}
}
