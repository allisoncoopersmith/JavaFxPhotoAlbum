package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import application.AdminSubsystemController;


import objects.*;



/**
 * This class functions as the backend for the administrator's subsystem 
 */

public class AdminSubsystemController { 
	
	/**
	 * This button allows the admin to delete a user
	 */
	@FXML
	Button DeleteUserButton;
	/**
	 * This button allows the admin to add a user
	 */
	@FXML
	Button AddNewUserButton;
	/**
	 * This button brings the user to the exit window
	 */
	@FXML
	Button ExitButton;
	/**
	 * This textfield allows the admin to enter a username for a new user
	 */
	@FXML
	TextField UserNameTextField;
	/**
	 * This is a list of users registered in the photos app
	 */
	@FXML
	ListView<User> listView=new ListView<User>();

	/**
	 * This displays the registered users in the system
	 */
	ObservableList<User> obsList; 
	/**
	 * This is a list of all users
	 */
	userList userList;
	/**
	 * This is the current user
	 */
	User currUser;
	/**
	 * This is the window's stqage
	 */
	Stage primaryStage;
	

	/**
	 * Launches the admin's window
	 * @param	mainstage
	 */
	

	public void start (Stage mainstage) {
		primaryStage = mainstage;

		if (!listContainsAdmin(userList.getUsers())){
			User admin = new User("admin");
			userList.addUser(admin);
		}

		obsList = FXCollections.observableArrayList(userList.getUsers());	
		listView.setItems(obsList);
	}
	/**
	 * Launches the exit window if the user presses the exit button
	 * @throws 	IOException 
	 * @param	event
	 */

	public void exitButtonHandler(ActionEvent event) throws IOException {


			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ExitWindow.fxml"));
			loader.setController(new exitWindowController());
			Parent root = loader.load();
			Scene scene = new Scene(root);
			exitWindowController controller = loader.getController();
			controller.prevWindow(0);
			controller.userList = userList;
			controller.currUser=currUser;
			controller.start(primaryStage);
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.show();
		}

	

	/**
	 * Allows the admin to add in a new user
	 * @param	event
	 */

	public void addNewUserButtonHandler(ActionEvent event) {
		

		String userName = UserNameTextField.getText();
		if (userName.length()==0) {
			missingFields();
			return;
		}
		User checkUser=userList.isUserInList(userName);

		if (checkUser==null) {
			User newUser = new User(userName);
			userList.addUser(newUser);
			obsList.add(newUser);
			UserNameTextField.setText("");
			return;

		}
		Alert alert = 
				new Alert(AlertType.INFORMATION);
		alert.setTitle("Error");
		alert.setHeaderText("User already in list");
		String content = "There is already a user with this name";
		alert.setContentText(content);
		alert.showAndWait();
		UserNameTextField.setText("");
	}

	/**
	 * Allows the admin to delete a user, so long as they are not trying to delete the admin
	 * @param	event
	 */
	public void deleteUserButtonHandler(ActionEvent event) {
		if (obsList.isEmpty()) { //can't edit on an empty list
			printIfEmpty();
			return;
		}

		User selectedUser = listView.getSelectionModel().getSelectedItem();
		if (selectedUser==null) {
			Alert alert = 
					new Alert(AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText("No selection");
			String content = "Make a selection";
			alert.setContentText(content);
			alert.showAndWait();
			return;
		}
		int index = listView.getSelectionModel().getSelectedIndex();

		if (selectedUser.getUsername().equals("admin")) {
			cantDeleteAdmin();
			return;
		}

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText("Confirm deletion");
		alert.setContentText("Are you sure you want to delete this user?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			obsList.remove(selectedUser);
			userList.deleteUser(selectedUser);
			listView.getSelectionModel().select(index++);
			
			if (selectedUser.getUsername().equals("stock")) {
				userList.stockDeleted();
			}
		} 
		else {
			listView.getSelectionModel().select(index);
			return;		
		}
	}
	/**
	 * A helper method to make sure there is an instance of admin in the list
	 * @param	userlist
	 * @return	true if the admin is in the list
	 */

	private boolean listContainsAdmin (ArrayList<User> users) {
		for (User user: users) {
			if (user.getUsername().equals("admin")) {
				return true;
			}			
		}
		return false;
	}



	/**
	 * This method is called if the admin attemps to delete the admin account
	 */

	private void cantDeleteAdmin() {
		Alert alert = 
				new Alert(AlertType.INFORMATION);
		alert.setTitle("Error");
		alert.setHeaderText("Can't delete admin");
		String content = "Admin cannot be deleted";
		alert.setContentText(content);
		alert.showAndWait();

	}

	/**
	 * This method is called if the admin attemps to edit an empty list
	 */

	private void printIfEmpty () { 
		Alert alert = 
				new Alert(AlertType.INFORMATION);
		alert.setTitle("Error");
		alert.setHeaderText("Empty list");
		String content = "List is empty";
		alert.setContentText(content);
		alert.showAndWait();

	}
	public void missingFields() {
		Alert alert = 
				new Alert(AlertType.INFORMATION);
		alert.setTitle("Error");
		alert.setHeaderText("Missing fields");
		String content = "Fill out all the required fields";
		alert.setContentText(content);
		alert.showAndWait();
	}
	


}
