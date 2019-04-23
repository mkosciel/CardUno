package playerScreen.controller;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import game.core.Card;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * @author £ukasz Wnêk
 * @author Micha³ Koœcielak
 */


/**
 * @author £ukasz Wnêk
 */
public class GameTableController {

	private static final Double cardWidth = 72d;
	private static final Double cardHeigth = 96d;

	private BufferedReader in = null;
	private PrintWriter out = null;
	private BufferedReader inGate = null;
	private PrintWriter outGate = null;
	private BufferedReader inRaspond = null;
	private PrintWriter outRespond = null;
	private Stage rootStage = null;

	private boolean isConnectionOpenned = false;
	private boolean hasToBeClosed = false;

	@FXML
	private Text CardLabel;

	@FXML
	private AnchorPane CenterField;

	@FXML
	private AnchorPane PlayerBottomDeck;

	private ArrayList<Card> playersCards = new ArrayList<>();
	private Integer oponentCount = 0;
	private Card actualCard = null;

	@FXML
	private AnchorPane PlayerUpDeck;

	@FXML
	private ImageView cardOnTable;

	@FXML
	private Button drawButton;

	@FXML
	private Label messageLabel;

	// przycisk dobrania karty
	@FXML
	void drawButtonOnAction(ActionEvent event) {
		if (!isConnectionOpenned)
			return;
		out.println("draw");
	}

	@FXML
	private Button reload;

	@FXML
	void reloadButton(ActionEvent event) {
		if (actualCard == null)
			return;
		sync();
	}

	@FXML
	void mouseMoved(MouseEvent event) {
		if (actualCard == null)
			return;
		sync();
	}

	// dodanie karty gracza + nadanie jej w³aœciwoœci
	private void addCardToBottomDeck(Card card) {
		Image cardImg = cardToImage(card);
		ImageView cardView = new ImageView();
		cardView.setImage(cardImg);
		cardView.setOnMouseEntered(event -> CardLabel.setText(card.toString()));
		cardView.setOnMouseExited(event -> CardLabel.setText(""));
		cardView.setOnMouseClicked(event -> {
			if (canPlayCard(playersCards.get(PlayerBottomDeck.getChildren().indexOf(cardView)))) {
				ChangeCardOnTop(playersCards.get(PlayerBottomDeck.getChildren().indexOf(cardView)));
				playersCards.remove(PlayerBottomDeck.getChildren().indexOf(cardView));
				PlayerBottomDeck.getChildren().remove(cardView);
				reloadBottomCardView();
			}
		});
		PlayerBottomDeck.getChildren().add(cardView);
		reloadBottomCardView();
	}
	
	/**
	 * @author Micha³ Koœcielak
	 */
	// próba zagrania karty z jednoczesn¹ prób¹ sprawdzenia po³¹czenia
	private boolean canPlayCard(Card card) {

		System.out.println("trying to play");

		if (!isConnectionOpenned)
			return false;

		out.println("play " + playersCards.indexOf(card));

		try {
			String respond = inRaspond.readLine();
			System.out.println("resond " + respond);
			if (respond.startsWith("OK")) {
				return true;
			} else if (respond.startsWith("NOT")) {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * @author £ukasz Wnêk
	 */
	// usuniêcie karty z do³u
	public void removeCardFromBottomDeck(int id) {
		try {
			PlayerBottomDeck.getChildren().remove(id);
			playersCards.remove(id);
			reloadBottomCardView();
		} catch (Exception e) {
			System.out.println("Error while removing card " + e.getMessage());
		}
	}

	// dodanie karty graczowi górnemu
	private void addCardToUpDeck() {
		Image img = new Image(getClass().getResourceAsStream("cards/unoUp.png"));
		ImageView cardView = new ImageView();
		cardView.setImage(img);
		PlayerUpDeck.getChildren().add(cardView);
		reloadUpCardView();
	}

	// zabranie karty graczowi górnemu
	public void removeCardFromUpDeck(int id) {
		try {
			PlayerUpDeck.getChildren().remove(id);
			reloadUpCardView();
		} catch (Exception e) {
			System.out.println("Error while removing card " + e.getMessage());
		}
	}

	// wczytanie obrazka karty
	private Image cardToImage(Card card) {
		Integer cardNumber = card.getNumber();
		Integer cardColor = card.getColor();

		BufferedImage bufferedImage = new BufferedImage(cardWidth.intValue(), cardHeigth.intValue(),
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = bufferedImage.createGraphics();
		g2d.setColor(Color.white);
		g2d.fillRect(0, 0, cardWidth.intValue(), cardHeigth.intValue());

		Image image = SwingFXUtils.toFXImage(bufferedImage, null);

		try {
			image = new Image(getClass().getResourceAsStream("cards/" + cardNumber + "-" + cardColor + ".png"));
		} catch (Exception e) {
			System.out.println("Error loading img: " + e.getMessage());
		}

		return image;
	}

	// zmiana karty
	private void ChangeCardOnTop(Card card) {
		cardOnTable.setImage(cardToImage(card));
		actualCard = card;
	}

	// odœwie¿enie widoku kart (mechanika nak³adania)
	private void reloadBottomCardView() {
		if (PlayerBottomDeck.getChildren().size() == 1) {
			AnchorPane.setRightAnchor(PlayerBottomDeck.getChildren().get(0), 0d);
			return;
		}

		for (int i = 0; i < PlayerBottomDeck.getChildren().size(); i++) {
			AnchorPane.setRightAnchor(PlayerBottomDeck.getChildren().get(i),
					i * (PlayerBottomDeck.getWidth() - cardWidth) / (PlayerBottomDeck.getChildren().size() - 1));
		}
	}

	// odœwie¿enie widoku kart (mechanika nak³adania)
	private void reloadUpCardView() {
		if (PlayerUpDeck.getChildren().size() == 1) {
			AnchorPane.setRightAnchor(PlayerUpDeck.getChildren().get(0), 0d);
			return;
		}

		for (int i = 0; i < PlayerUpDeck.getChildren().size(); i++) {
			AnchorPane.setRightAnchor(PlayerUpDeck.getChildren().get(i),
					i * (PlayerUpDeck.getWidth() - cardWidth) / (PlayerUpDeck.getChildren().size() - 1));
		}
	}

	/**
	 * @author Micha³ Koœcielak
	 */
	// ustawienie strumieni wejœcia/wyjœcia
	public void setIO(BufferedReader in, PrintWriter out, BufferedReader inGate, PrintWriter outGate,
			BufferedReader inRespond, PrintWriter outRespond, String name, Stage stage) {
		this.in = in;
		this.out = out;
		this.inGate = inGate;
		this.outGate = outGate;
		this.inRaspond = inRespond;
		this.outRespond = outRespond;
		String name1 = name;

		Commands commands = new Commands(in, out, this, name);
		commands.start();

		rootStage = stage;
	}

	// w³¹czenie po³¹czenia
	public void openConnection() {
		isConnectionOpenned = true;
	}

	// wy³¹czenie po³czenia
	public void closeConnection() {
		isConnectionOpenned = false;
	}

	/**
	 * @author £ukasz Wnêk
	 */
	// wynchronizacja sto³u
	private void sync() {

		if (hasToBeClosed)
			rootStage.close();

		int n = PlayerBottomDeck.getChildren().size();
		while (n-- > 0) {
			PlayerBottomDeck.getChildren().remove(0);
		}

		for (Card ca : playersCards) {
			addCardToBottomDeck(ca);
		}
		ChangeCardOnTop(actualCard);

		n = PlayerUpDeck.getChildren().size();
		while (n-- > 0) {
			PlayerUpDeck.getChildren().remove(0);
		}

		for (int i = 0; i < oponentCount; i++) {
			addCardToUpDeck();
		}
	}

	public void setToCloseTheWindow() {
		hasToBeClosed = true;
	}

	// ustawienie karty na górze sto³u
	private void setTopCard(Card card) {
		actualCard = card;
	}

	// dodanie kart oponentowi
	private void addOpponent() {
		oponentCount++;
	}

	// zabranie karty oponentowi
	private void removeOpponent() {
		oponentCount--;
	}

	/**
	 * @author Micha³ Koœcielak
	 */
	// klasa w¹tku zajmuj¹cego siê obs³ug¹ komend z silnika
	class Commands extends Thread {
		BufferedReader in;
		PrintWriter out;
		GameTableController cntl;
		String name;

		// ustawienie strumieni wejœcia/wyjœcia
		Commands(BufferedReader in, PrintWriter out, GameTableController cntl, String name) {
			this.in = in;
			this.out = out;
			this.cntl = cntl;
			this.name = name;
		}

		// uruchomienie w¹tku
		public void run() {
			try {
				while (true) {
					System.out.println("Getting command: ");
					String pol = in.readLine();
					System.out.println(pol);
					if (pol.startsWith("NAME")) {
						out.println(name);
					} else if (pol.startsWith("ADD ")) {
						String[] roz = pol.split(" ");
						Integer id = Integer.parseInt(roz[1]);
						Integer color = null;
						if (Card.NotColored.indexOf(id) == -1) {
							color = Integer.parseInt(roz[2]);
						}
						cntl.playersCards.add(new Card(id, color));

					} else if (pol.startsWith("STARTING")) {
						String[] roz = pol.split(" ");
						Integer id = Integer.parseInt(roz[1]);
						Integer color = Integer.parseInt(roz[2]);
						System.out.println(id + " " + color);
						cntl.setTopCard(new Card(id, color));
					} else if (pol.startsWith("MESSAGE ")) {
						String message = pol.replace("MESSAGE ", "");
						// messageLabel.setText(message);
					} else if (pol.startsWith("MESSAGE_END ")) {
						String message = pol.replace("MESSAGE_END ", "");
						JFrame frame = new JFrame();
						JOptionPane.showMessageDialog(frame, message, "Koniec gry!", JOptionPane.INFORMATION_MESSAGE);
						frame.dispose();
						setToCloseTheWindow();

					} else if (pol.startsWith("ADD_OPP")) {
						cntl.addOpponent();
					} else if (pol.startsWith("REM_OPP")) {
						cntl.removeOpponent();
					}
				}
			} catch (Exception e) {
				System.out.println("B³¹d komend: " + e.getMessage());
				setToCloseTheWindow();
			}
			System.out.println("Thread com end");
		}
	}
}
