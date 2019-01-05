package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
/**
 * This class functions as the application's "main". It is the first class to execute
 */

public class Photos extends Application {
	@Override
	/**
	 * This method staerts the program by calling the login window's controller
	 * @throws	 IOException if the login window's fxml file can't be found
	 * @param	 primaryStage
	 */
	public void start(Stage primaryStage) throws IOException {	
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginPortal.fxml"));
		  loader.setController(new loginController());
		  Parent root = loader.load();
		  Scene scene = new Scene(root);
		  loginController controller = loader.getController();
		  controller.start(primaryStage);
		  primaryStage.setResizable(false);
		  primaryStage.setScene(scene);
		  primaryStage.show(); 
		} 
	
	/**
	 * This method is the backup to the start method
	 * @param 	args
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
