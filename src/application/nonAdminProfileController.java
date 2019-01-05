package application;
import java.io.File;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import objects.*;
/**
 * This class functions as the backend for the user profile window
 */
public class nonAdminProfileController {
	/**
	 * This ImageView displays the first image in the selected album
	 */
	@FXML 
	ImageView imageView;
	/**
	 * This button opens a specified album
	 */
	@FXML
	Button ViewAlbumButton;
	/**
	 * This button renames the selected album
	 */
	@FXML 
	Button RenameAlbumButton;
	/**
	 * This button creates a new album
	 */
	@FXML 
	Button AddAlbumButton;
	/**
	 * This button launches the search window
	 */
	@FXML
	Button SearchButton;
	/**
	 * This button returns the user to the previous window
	 */
	@FXML
	Button ExitButton;
	/**
	 * This button deletes the selected album
	 */
	@FXML
	Button DeleteAlbumButton;
	/**
	 * This ListView displays a list of albums
	 */
	@FXML
	ListView<Album> AlbumListView;
	/**
	 * This TextField allows the user to type in a new name for an album
	 */
	@FXML
	TextField renameAlbumTF;
	/**
	 * This textfield allows the user to enter a name for a new album
	 */
	@FXML
	TextField addAlbumTF;
	
	/**
	 * This ObservableList keeps track of the list of albums
	 */

	ObservableList<Album> obsList; 
	/**
	 * This stage is the window's stage
	 */
	Stage primaryStage;
	/**
	 * This user represents the current user on the system
	 */
	User currUser;
	/**
	 * This userList represents a list of all the users registered with the app
	 */
	userList userList;
	
	/**
	 * This method launches the window
	 * @param	mainstage
	 */

	public void start(Stage mainstage) {
		primaryStage=mainstage;

		obsList = FXCollections.observableArrayList(currUser.getAlbums());	

		AlbumListView.setItems(obsList); 

		AlbumListView.setItems(obsList); 
		AlbumListView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> showPhotoDetails(primaryStage) );
		AlbumListView.getSelectionModel().select(0);	

	}

	/**
	 * This method shows the currently selected album's first photo's thumbnail 
	 ** @param	primaryStage
	 * 
	 */

	public void showPhotoDetails(Stage primaryStage) {
		if (obsList.isEmpty()) {
			imageView.setImage(null);
			return;
		}
		Album selectedAlbum = AlbumListView.getSelectionModel().getSelectedItem();
		if (selectedAlbum ==null) {
			AlbumListView.getSelectionModel().select(0);
			selectedAlbum= AlbumListView.getSelectionModel().getSelectedItem();
		}
		if (selectedAlbum.getPhotos().size() > 0) {
			ArrayList<Photo> albumPhotos = selectedAlbum.getPhotos();
			Photo selectedPhoto = albumPhotos.get(0);
			File file = selectedPhoto.getImage().getImageFile();
			Image image = new Image(file.toURI().toString());        
			imageView.setImage(image);
			return;
		}
		imageView.setImage(null);
	}
	/**
	 * Shows the currently selected album's first photo's thumbnail 
	 * @throws	IOException if the album display's FXML file can't be found
	 ** @param	event
	 */

	public void ViewAlbumButtonHandler(ActionEvent event) throws IOException {
		if (obsList.isEmpty()) {
			printIfEmpty();
			return;
		}

		Album selectedAlbum =AlbumListView.getSelectionModel().getSelectedItem();
		if (selectedAlbum==null) {
			return;

		}

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/IndividualAlbumDisplay.fxml"));
		loader.setController(new individualAlbumController());
		Parent root = loader.load();
		Scene scene = new Scene(root);
		individualAlbumController controller = loader.getController(); 
		controller.userList = userList;
		controller.currUser=currUser;
		controller.currentAlbumOpen=selectedAlbum;
		controller.start(primaryStage);
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();


	}
	/**
	 * Allows the user to rename the selected album 
	 * @param	event
	 */
	public void RenameAlbumButtonHandler(ActionEvent event) {
		if (obsList.isEmpty()) {
			printIfEmpty();
			return;
		}


		String newName = renameAlbumTF.getText();

		if (newName.length() == 0) {
			Alert alert = 
					new Alert(AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText("No name entered");
			String content = "Enter a name";
			alert.setContentText(content);
			alert.showAndWait();
			return;
		}
		if (currUser.checkIfAlbumExists(newName)) {
			Alert alert = 
					new Alert(AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText("Album already exists");
			String content = "There is already an album with this name";
			alert.setContentText(content);
			alert.showAndWait();
			return;

		}
		Album selectedAlbum =AlbumListView.getSelectionModel().getSelectedItem();
		if (selectedAlbum==null) {
			return;

		}
		currUser.deleteAlbumByName(selectedAlbum.getName());
		obsList.remove(selectedAlbum);
		selectedAlbum.setName(newName);
		currUser.addAlbum(selectedAlbum);
		obsList.setAll(currUser.getAlbums());
		AlbumListView.setItems(this.obsList);

	}
	/**
	 * Allows the user to add a new album
	 * @param	event
	 */
	public void AddAlbumButtonHandler(ActionEvent event) {

		if (addAlbumTF.getText().equals("")) {
			missingFields();
			return;
		}

		String newName = addAlbumTF.getText();
		if (newName == null) {
			Alert alert = 
					new Alert(AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText("No name entered");
			String content = "Enter a name";
			alert.setContentText(content);
			alert.showAndWait();
			return;
		}
		if (currUser.checkIfAlbumExists(newName)) {
			Alert alert = 
					new Alert(AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText("Album already exists");
			String content = "There is already an album with this name";
			alert.setContentText(content);
			alert.showAndWait();
			return;

		}

		Album newAlbum = new Album(newName);
		currUser.addAlbum (newAlbum);
		obsList.add(new Album(newName));

	}
	/**
	 * Opens the search window
	 * @throws	IOException if the search window's FXML file can't be found
	 * @param	event
	 */
	public void SearchButtonHandler(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SearchEnginePortal.fxml"));
		loader.setController(new searchEngineController());
		Parent root = loader.load();
		Scene scene = new Scene(root);
		searchEngineController controller = loader.getController(); 
		controller.userList = userList;
		controller.currUser=currUser;
		controller.start(primaryStage);
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();

	}
	/**
	 * Opens the exit window, letting the user logout or quit the application
	 * @throws	 IOException if the exit window's FXML file can't be found
	 * @param	 event
	 */
	public void ExitButtonHandler(ActionEvent event) throws IOException {


		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ExitWindow.fxml"));
		loader.setController(new exitWindowController());
		Parent root = loader.load();
		Scene scene = new Scene(root);
		exitWindowController controller = loader.getController();
		controller.prevWindow(1);
		controller.userList = userList;
		controller.currUser=currUser;		
		controller.start(primaryStage);
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();


	}
	/**
	 * Deletes the selected album
	 * @param	event
	 */
	public void DeleteAlbumButtonHandler(ActionEvent event) {
		if (obsList.isEmpty()) {
			printIfEmpty();
			return;
		}
		Album selectedAlbum = AlbumListView.getSelectionModel().getSelectedItem();
		if (selectedAlbum==null) {
			return;
		}
		int index = AlbumListView.getSelectionModel().getSelectedIndex();



		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText("Confirm deletion");
		alert.setContentText("Are you sure you want to delete this album?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			currUser.deleteAlbumByName(selectedAlbum.getName());
			obsList.remove(selectedAlbum);
			AlbumListView.getSelectionModel().select(index++);
		} 
		else {
			AlbumListView.getSelectionModel().select(index);
			return;		
		}


	}

	/**
	 * This method executes if the list is empty
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
