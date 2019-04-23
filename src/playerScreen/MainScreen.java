package playerScreen;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import playerScreen.controller.MainScreenController;

/**
 * @author £ukasz Wnêk
 *
 */
public class MainScreen extends Application{

	/**
	 * Ekran powitalny + logowanie do gry
	 * JavaFX
	 * */
	@Override
	public void start(Stage primaryStage) throws Exception {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("fxml/MainScreenWindow.fxml"));
		AnchorPane root = loader.load();
		
		primaryStage.setTitle("UNO!");
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("controller/cards/icon.png")));
		primaryStage.setScene(new Scene(root));
		primaryStage.setResizable(false);
		primaryStage.sizeToScene();
		primaryStage.show();
		
		MainScreenController cont = loader.getController();
		cont.setAll(root, primaryStage);

		System.out.println("Clossssss...");
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
