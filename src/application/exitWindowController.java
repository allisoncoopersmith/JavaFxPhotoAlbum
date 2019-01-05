package application;


import java.io.IOException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import objects.*;

/**
 * This class functions as the backend for the exit window
 */

public class exitWindowController {
	/**
	 * This button allows the users to log out
	 */
	@FXML
	Button logoutBtn;
	/**
	 * This button allows the user to quit the application
	 */
	@FXML
	Button quitBtn;
	/**
	 * This button returns the user to the previous screen
	 */
	@FXML
	Button cancelBtn;	
	/**
	 * This window's stag
	 */
	Stage primaryStage;
	/**
	 * An int value to track which window was open previously
	 */
	int previousWindow;
	/**
	 * A list of all the users
	 */
	userList userList;
	/**
	 * The current user
	 */
	User currUser;
	
	/**
	 * This method launches the exit window
	 * @param	mainstage
	 */

	public void start (Stage mainstage) {
		primaryStage=mainstage;

	}
	/**
	 * This method brings the user back to the login portal if they click the logout button
	 * @throws	IOException if the login portal's fxm file can't be found
	 * @param	event
	 */
	
	public void handleLogoutBtn(ActionEvent event) throws  IOException {
		userList.writeUsers(userList);

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginPortal.fxml"));
		loader.setController(new loginController());
		Parent root = loader.load();
		Scene scene = new Scene(root);
		loginController controller = loader.getController();
		controller.userList = userList;
		controller.currUser=currUser;	
		controller.start(primaryStage);
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	/**
	 * This method ends the program if the user clicks the quit button, serializing the data
	 * @throws	IOException if the file to write to isn't found
	 * @param	event
	 */
	public void handleQuitBtn (ActionEvent event) throws IOException {
		userList.writeUsers(userList);

		System.exit(0);
	}
	/**
	 * This method determines what the previous window was, and brings the user back to it
	 * @throws	IOException if the previous window's FXML can't be found
	 * @param	event
	 */
	public void handleCancelBtn(ActionEvent event) throws IOException {
		if (previousWindow==0) {
			returnToAdminWindow();
		}
		if (previousWindow==1) {
			returnToNonAdminWindow();
		}

	}
	/**
	 * This method keeps track of which window the user was at before they opened the exit window
	 * @param	prevWin	int value holding the previous window's value
	 */
	public void prevWindow(int prevWin) {
		previousWindow=prevWin;
	}
	
	/**
	 * This method brings the user back to the non admin user profile window
	 * @throws	IOException if the previous window's FXML can't be found
	 */
	
	public void returnToNonAdminWindow() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/NonAdminUserProfile.fxml"));
		loader.setController(new nonAdminProfileController());
		Parent root = loader.load();
		Scene scene = new Scene(root);
		nonAdminProfileController controller = loader.getController();
		controller.userList = userList;
		controller.currUser=currUser;
		controller.start(primaryStage);
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	
	/**
	 * This method brings the user back to the  admin user profile window
	 * @throws	IOException if the previous window's FXML can't be found
	 */
	
	public void returnToAdminWindow() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AdminSubsystem.fxml"));
		loader.setController(new AdminSubsystemController());
		Parent root = loader.load();
		Scene scene = new Scene(root);
		AdminSubsystemController controller = loader.getController();
		controller.userList = userList;
		controller.currUser=currUser;
		controller.start(primaryStage);
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	

}



