package playerScreen.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import playerScreen.GameTable;

/**
 * @author Micha³ Koœcielak
 *
 */
public class MainScreenController {

	private AnchorPane rootPane;
	private Stage rootStage;

	@FXML
	private ImageView titleImage;

	@FXML
	private Button ButtonJoin;

	@FXML
	private TextField TextFieldAdress;

	@FXML
	private TextField TextFieldUser;

	// post-konstruktor
	public void setAll(AnchorPane root, Stage rootS) {
		rootPane = root;
		rootStage = rootS;
	}

	@FXML
	void JoinButtonAction(ActionEvent event) {
		// próba po³¹czenia do serwera
		String playerName = TextFieldUser.getText();
		String adress = TextFieldAdress.getText();

		if (playerName == null)
			return;

		final Integer PORT = 8910;
		final Integer GatePORT = 8911;
		final Integer RespondPORT = 8912;

		final int TIME_OUT = 3600 * 1000;

		try {

			System.out.println("£¹czenie...");
			Socket socket = new Socket(adress, PORT);
			socket.setSoTimeout(TIME_OUT);
			Socket gate = new Socket(adress, GatePORT);
			gate.setSoTimeout(TIME_OUT);
			Socket respond = new Socket(adress, RespondPORT);
			respond.setSoTimeout(TIME_OUT);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedReader inGate = new BufferedReader(new InputStreamReader(gate.getInputStream()));
			BufferedReader inResond = new BufferedReader(new InputStreamReader(respond.getInputStream()));

			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			PrintWriter outGate = new PrintWriter(gate.getOutputStream(), true);
			PrintWriter outRespond = new PrintWriter(respond.getOutputStream(), true);

			// utworzenie sto³u po poprawnym po³¹czeniu
			GameTable gameTable = new GameTable();
			gameTable.show(rootStage, in, out, inGate, outGate, inResond, outRespond, playerName);

			rootStage.close();

		} catch (Exception e) {
			System.out.println("B³¹d podczas po³¹czenia: " + e.getMessage());
		}
	}
}
