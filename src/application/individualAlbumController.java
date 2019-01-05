package application;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import objects.*;

/**
 * This class functions as the backend for the individual album display window
 */
public class individualAlbumController {
	/**
	 * This button allows the user to return to their profile
	 */
	@FXML
	Button ReturnToUserPortalButton;
	/**
	 * This button copies the selected photo and opens a window to paste it into a different album
	 */
	@FXML
	Button CopyPhotoButton;
	/**
	 * This ListView holds all the user's albums
	 */
	@FXML
	ListView<Photo> AlbumListView;
	/**
	 * This button allows the users to upload a photo
	 */
	@FXML 
	Button AddPhotoButton;
	/**
	 * This button allows the users to delete a photo
	 */
	@FXML
	Button DeletePhotoButton;
	/**
	 * This button launches a slideshow of the images in the album
	 */
	@FXML
	Button SlideshowButton;
	@FXML
	/**
	 * This button opens a window to edit an individual photo
	 */
	Button EditPhotoButton;
	/**
	 * This button opens a window to move a photo
	 */
	@FXML
	Button MovePhotoButton;
	/**
	 * This imageview displays a photo's thumbnail
	 */
	@FXML
	ImageView imageView;
	
	/**
	 * This textfield displays the album's size
	 */
	@FXML
	TextField albumSizeTf;
	/**
	 * This textfield displays a photo's caption
	 */
	@FXML
	TextField captionTf;
	/**
	 * This textfield displays the album's name
	 */
	@FXML
	TextField albumNameTf;

	/**
	 * This textfield displays the oldest photo's date
	 */
	@FXML
	TextField oldestPhotoTf;
	/**
	 * This textfield displays the newest photo's date
	 */
	@FXML
	TextField newestPhotoTf;

	/**
	 * This window's stage
	 */
	Stage primaryStage;
	/**
	 * This album currently being displayed
	 */
	Album currentAlbumOpen;
	/**
	 * This current user logged into the system
	 */
	User currUser;
	/**
	 * A list of all users
	 */
	userList userList;
	/**
	 * An ObservableList to hold the albums
	 */
	ObservableList<Photo> obsList; 


	/**
	 * Launches the individual album view window
	 * @param	mainstage
	 */
	public void start (Stage mainstage) {
		primaryStage=mainstage;
		obsList = FXCollections.observableArrayList(currentAlbumOpen.getPhotos());	
		AlbumListView.setItems(obsList); 
		AlbumListView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> showPhotoDetails(primaryStage) );
		AlbumListView.getSelectionModel().select(0);	
		albumNameTf.setText(currentAlbumOpen.getName());
		albumSizeTf.setText(Integer.toString(currentAlbumOpen.getPhotos().size()));
		setOldestNewest();

	}

	/**
	 * Shows the currently selected photo's thumbnail and caption
	 * @param	primaryStage
	 */

	public void showPhotoDetails(Stage primaryStage) {
		if (obsList.isEmpty()) {
			imageView.setImage(null);
			captionTf.setText("");
			return;
		}
		Photo selectedPhoto = AlbumListView.getSelectionModel().getSelectedItem();
		if (selectedPhoto ==null) {
			AlbumListView.getSelectionModel().select(0);
			selectedPhoto = AlbumListView.getSelectionModel().getSelectedItem();
		}
		File file = selectedPhoto.getImage().getImageFile();
		Image image = new Image(file.toURI().toString());        
		imageView.setImage(image);
		captionTf.setText(selectedPhoto.getCaption());

	}
	/**
	 * Allows the user to move a photo
	 * @throws	IOException if the move/copy photo window FXML isn't found
	 * @param	event
	 */
	public void MovePhotoButtonHandler(ActionEvent event) throws IOException {
		Photo copiedPhoto = AlbumListView.getSelectionModel().getSelectedItem();
		if (copiedPhoto==null) {
			return;
		}

		currentAlbumOpen.removePhotoByImage(copiedPhoto);


		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MoveCopyPhoto.fxml"));
		loader.setController(new moveCopyPhotoController());
		Parent root = loader.load();
		Scene scene = new Scene(root);
		moveCopyPhotoController controller = loader.getController();
		controller.copiedPhoto= copiedPhoto;
		controller.albumCopiedFrom=currentAlbumOpen;
		controller.userList = userList;
		controller.currUser=currUser;
		controller.start(primaryStage);
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();

	}
	/**
	 * This button opens a window to edit an individual photo
	 * @throws	IOException if the individual photo view fxml isn't found
	 * @param	event
	 */
	public void ViewEditPhotoButtonHandler(ActionEvent event) throws IOException {
		Photo currPhoto = AlbumListView.getSelectionModel().getSelectedItem();
		if (currPhoto==null) {
			return;
		}
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/IndividualPhotoView.fxml"));
		loader.setController(new individualPhotoController());
		Parent root = loader.load();
		Scene scene = new Scene(root);
		individualPhotoController controller = loader.getController();
		controller.currAlbum = currentAlbumOpen;
		controller.currPhoto=currPhoto;
		controller.userList = userList;
		controller.currUser=currUser;
		controller.prevWindow=1;
		controller.start(primaryStage);
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show(); 

	}
	/**
	 * Allows the user to delete a photo
	 * @param	event
	 */
	public void DeletePhotoButtonHandler(ActionEvent event) {
		if (obsList.isEmpty()) {
			printIfEmpty();
			return;
		}
		Photo selectedPhoto = AlbumListView.getSelectionModel().getSelectedItem();
		if (selectedPhoto==null) {
			return;
		}
		int index = AlbumListView.getSelectionModel().getSelectedIndex();



		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText("Confirm deletion");
		alert.setContentText("Are you sure you want to delete this photo?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			currentAlbumOpen.removePhotoByImage(selectedPhoto);
			obsList.remove(selectedPhoto);
			AlbumListView.getSelectionModel().select(index++);
		} 
		else {
			AlbumListView.getSelectionModel().select(index);
			return;		

		}
		setOldestNewest();
	}
	/**
	 * Allows the user to open a slideshow of images in the album
	 * @throws	IOException if the slideshow window FXML isn't found
	 * @param	event
	 */
	public void SlideshowButtonHandler(ActionEvent event) throws IOException {
		if (currentAlbumOpen.getPhotos().size()==0) {
			Alert alert = 
					new Alert(AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText("Album is empty");
			String content = "Add some photos first";
			alert.setContentText(content);
			alert.showAndWait();
			return;
			
		}
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AlbumSlideshow.fxml"));
		loader.setController(new albumSlideshowController());
		Parent root = loader.load();
		Scene scene = new Scene(root);
		albumSlideshowController controller = loader.getController();
		controller.currAlbum=currentAlbumOpen;
		controller.userList = userList;
		controller.currUser=currUser;
		controller.prevWindow=1;
		controller.start(primaryStage);
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();

	}
	/**
	 * Allows the user to add a photo
	 * @param	event
	 */
	public void AddPhotoButtonHandler(ActionEvent event) {


		FileChooser fc = new FileChooser();
		fc.setTitle("Choose a Photo");
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("jpg or png or jpeg files only (*.jpg, *.png, *jpeg)",
				"*.jpg", "*.png", "*.jpeg");

		fc.getExtensionFilters().add(extFilter);
		File file = fc.showOpenDialog(primaryStage);

		if (file == null) {
			return;
		} 
		else {
			for (Photo p : currentAlbumOpen.getPhotos()) {
				if (p.getImage().getImageFile().equals(file)) {
					Alert alert = 
							new Alert(AlertType.INFORMATION);
					alert.setTitle("Error");
					alert.setHeaderText("Photo already exists");
					String content = "This photo is already in the album";
					alert.setContentText(content);
					alert.showAndWait();
					return;
				} 
			} 
		} 
		currUser.deleteAlbumByName(currentAlbumOpen.getName());
		currentAlbumOpen.addPhoto(new Photo(new photoImage(file), null));
		currUser.addAlbum(currentAlbumOpen);
		obsList.setAll(currentAlbumOpen.getPhotos());
		AlbumListView.setItems(this.obsList);
		AlbumListView.getSelectionModel().select(0);
		setOldestNewest();

	}
	/**
	 * Allows the user to return to their profile
	 * @throws	IOException if the user profile window FXML isn't found
	 * @param	event
	 */
	public void ReturnToUserPortalButtonHandler(ActionEvent event) throws IOException {
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
	 * This method copies a photo, deletes it from its source album, and opens up the moveCopyPhoto window
	 * @throws	IOException if the move/copy photo FXML file isn't found
	 * @param	event
	 */

	public void CopyPhotoButtonHandler(ActionEvent event) throws IOException {

		Photo copiedPhoto = AlbumListView.getSelectionModel().getSelectedItem();
		if (copiedPhoto==null) {
			return;
		}

		currentAlbumOpen.removePhotoByImage(copiedPhoto);


		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MoveCopyPhoto.fxml"));
		loader.setController(new moveCopyPhotoController());
		Parent root = loader.load();
		Scene scene = new Scene(root);
		moveCopyPhotoController controller = loader.getController();
		controller.copiedPhoto= copiedPhoto;
		controller.albumCopiedFrom=currentAlbumOpen;
		controller.copiedNotMoved=1;
		controller.userList = userList;
		controller.currUser=currUser;
		controller.start(primaryStage);
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	/**
	 * Prints if the user attempts to edit on an empty list
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
	/**
	 * Finds the date of the oldest and newest photos in the album, and displays them in the appropriate text fields
	 */
	public void setOldestNewest() {

		ArrayList<Photo> photos = currentAlbumOpen.getPhotos();
		ArrayList<photoImage> images = new ArrayList<photoImage>();
		
		for (Photo p: photos) {
			images.add(p.getImage());
		}
		
		Collections.sort(images);
		if (images.size()==0) {
			return;
		}
		oldestPhotoTf.setText(images.get(0).getDate().toString());
		newestPhotoTf.setText(images.get(images.size()-1).getDate().toString());

	}
}