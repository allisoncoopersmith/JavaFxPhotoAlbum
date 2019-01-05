package application;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import objects.*;

public class searchResultsController {
	/**
	 * displays the current photo
	 */
	@FXML
	ImageView imageView;
	/**
	 * displays all the photos in the search results
	 */
	@FXML
	ListView<Photo> searchPhotoListView;
	/**
	 * allows the user to view and edit a photo
	 */
	@FXML
	Button viewEditPhotoBtn;
	/**
	 * allows the user to create an album out of search results
	 */
	@FXML 
	Button createAlbumBtn;
	/**
	 * allows the user to launch a slideshow of result photos
	 */
	@FXML
	Button slideshowBtn;
	/**
	 * returns to the previous photo
	 */
	@FXML
	Button backBtn;
	/**
	 * allows the user to enter a name for a new album made of search results
	 */
	@FXML
	TextField newAlbumNameTf;
	
	/**
	 * stores a list of the photos in the result
	 */
	ObservableList<Photo> obsList;
	/**
	 * a list of all users
	 */
	userList userList;
	/**
	 * the album of search results
	 */
	Album currAlbum;
	/**
	 * the current user
	 */
	User currUser;
	/**
	 * the window's stage
	 */
	Stage primaryStage;
	

	/**
	 * This method launches the window
	 * @param	mainstage
	 */

	public void start(Stage mainstage) {
		primaryStage=mainstage;
		obsList = FXCollections.observableArrayList(currAlbum.getPhotos());	
		searchPhotoListView.setItems(obsList); 
		searchPhotoListView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> showPhotoDetails(primaryStage) );
		searchPhotoListView.getSelectionModel().select(0);	

	}

	/**
	 * Shows the currently selected photo's thumbnail and caption
	 * @param	primaryStage
	 */

	public void showPhotoDetails(Stage primaryStage) {
		if (obsList.isEmpty()) {
			imageView.setImage(null);
			return;
		}
		Photo selectedPhoto = searchPhotoListView.getSelectionModel().getSelectedItem();
		if (selectedPhoto ==null) {
			searchPhotoListView.getSelectionModel().select(0);
			selectedPhoto = searchPhotoListView.getSelectionModel().getSelectedItem();
		}
		File file = selectedPhoto.getImage().getImageFile();
		Image image = new Image(file.toURI().toString());        
		imageView.setImage(image);

	}
	/**
	 * Opens the view/edit photo window
	 * @param	event
	 * @throws	IOException
	 */
	
	public void viewEditBtnHandler (ActionEvent event) throws IOException {
		Photo currPhoto = searchPhotoListView.getSelectionModel().getSelectedItem();
		if (currPhoto==null) {
			return;
		}
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/IndividualPhotoView.fxml"));
		loader.setController(new individualPhotoController());
		Parent root = loader.load();
		Scene scene = new Scene(root);
		individualPhotoController controller = loader.getController();
		controller.currAlbum = currAlbum;
		controller.currPhoto=currPhoto;
		controller.userList = userList;
		controller.currUser=currUser;
		controller.prevWindow=2;
		controller.start(primaryStage);
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show(); 
		
	}
	/**
	 * Creates a new album and launches the album view window
	 * @param	event
	 * @throws	IOException
	 */
	public void createAlbumHandler(ActionEvent event) throws IOException {
		if (obsList.isEmpty()) {
			return;
		}
		String newName = newAlbumNameTf.getText();
		if (newName.length()==0) {
			Alert alert = 
					new Alert(AlertType.INFORMATION);
			alert.setTitle("No name");
			alert.setHeaderText("Name your album!");
			String content = "Enter a name for your new album";
			alert.setContentText(content);
			alert.showAndWait();
			return;
		}
		currAlbum.setName(newName);
		currUser.addAlbum(currAlbum);


		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/IndividualAlbumDisplay.fxml"));
		loader.setController(new individualAlbumController());
		Parent root = loader.load();
		Scene scene = new Scene(root);
		individualAlbumController controller = loader.getController(); 
		controller.userList = userList;
		controller.currUser=currUser;
		controller.currentAlbumOpen=currAlbum;
		controller.start(primaryStage);
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();

		
	}
	/**
	 * Opens the previous window
	 * @param	event
	 * @throws	IOException
	 */
	public void backBtnHandler(ActionEvent event) throws IOException {
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
	 * Opens the slideshow window
	 * @param	event
	 * @throws	IOException
	 */
	public void slideshowBtnHandler(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AlbumSlideshow.fxml"));
		loader.setController(new albumSlideshowController());
		Parent root = loader.load();
		Scene scene = new Scene(root);
		albumSlideshowController controller = loader.getController();
		controller.currAlbum=currAlbum;
		controller.userList = userList;
		controller.currUser=currUser;
		controller.prevWindow=2;
		controller.start(primaryStage);
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	
}
