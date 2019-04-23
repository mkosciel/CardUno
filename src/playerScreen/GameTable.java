package playerScreen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import playerScreen.controller.GameTableController;

import javax.swing.*;

/**
 * @author £ukasz Wnêk
 *
 */
public class GameTable {

	private Stage rootStage;
	private Stage stage;

	// uruchomienie sto³u
	public void show(Stage rootStage, BufferedReader in, PrintWriter out, BufferedReader inGate, PrintWriter outGate, BufferedReader inRespond, PrintWriter outRespond, String name) {
		this.rootStage = rootStage;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("fxml/GameTable.fxml"));
			AnchorPane root = loader.load();
			Stage stage = new Stage();
			this.stage = stage;
			
			stage.setTitle("The GAME! playing as " + name);
			stage.getIcons().add(new Image(getClass().getResourceAsStream("controller/cards/icon.png")));
			stage.setScene(new Scene(root));
			stage.setResizable(false);
			stage.sizeToScene();
			stage.show();

			// utworzenie kontrolera sto³u gry
			GameTableController cntl = loader.getController();
			cntl.setIO(in, out, inGate, outGate, inRespond, outRespond, name, stage);

			// w¹tek sprawdzaj¹cy mo¿liwoœæ wysy³ania komend
			Checker connectionChecking = new Checker(inGate, outGate, cntl);
			connectionChecking.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		rootStage.close();
	}

	
	
	class Checker extends Thread{
		BufferedReader in;
		PrintWriter out;
		GameTableController cntl;
		Checker(BufferedReader in, PrintWriter out, GameTableController cntl) {
			this.in = in;
			this.out = out;
			this.cntl = cntl;
		}
		
		public void run() {
			try {
				while(true) {
					System.out.println("Trying to gate");
					String pol = in.readLine();
					System.out.println("gate " + pol);
					if(pol.startsWith("OPEN")) {
						cntl.openConnection();
					} else if(pol.startsWith("CLOSE")){
						cntl.closeConnection();
					}
				}
			} catch (Exception e) {
				JFrame frame = new JFrame();
				JOptionPane.showMessageDialog(frame, "Connection lost. Close the program!");
				frame.dispose();
				cntl.setToCloseTheWindow();
				try {
					throw e;
				} catch (IOException e1) {
					System.out.println("B³¹d wejœcia/wyjœcia: " + e.getMessage());
				}
			}
			System.out.println("Thread end");
		}
	}
	
	

}
