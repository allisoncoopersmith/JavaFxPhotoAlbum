package application;
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
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import objects.*;

public class moveCopyPhotoController {

	/**
	 * This button returns the user to the previous window
	 */
	@FXML
	Button CancelMoveCopyButton;

	/**
	 * This button confirms the user wants to copy or move the selected photo
	 */

	@FXML
	Button ConfirmMoveCopyButton;

	/**
	 * This ListView displays a list of possible destination albums
	 */

	@FXML
	ListView<Album> DestinationAlbumList;

	/**
	 * This ListView displays a list of possible destination albums
	 */
	ObservableList<Album> obsList;
	/**
	 * This is the window's stage
	 */
	Stage primaryStage;
	/**
	 * This is the selected photo to copy from
	 */
	Photo copiedPhoto;
	/**
	 * This is the album the photo was copied from
	 */
	Album albumCopiedFrom;
	/**
	 * This is the current user
	 */
	User currUser;
	/**
	 * This is a list of registered users
	 */
	userList userList;
	/**
	 * This int value keeps track of whether a photo is being copied or moved
	 */
	int copiedNotMoved=0;
	/**
	 * This method launches the move/copy photo window
	 * @param	mainstage
	 */

	public void start (Stage mainstage) {

		primaryStage=mainstage;
		obsList = FXCollections.observableArrayList(currUser.getAlbums());	

		DestinationAlbumList.setItems(obsList); 

	}
	/**
	 * This method moves or copies the selected photo and then brings the user back to the individual album display
	 * @throws	IOException 
	 * @param	event
	 */

	public void ConfirmMoveCopyButtonHandler(ActionEvent event) throws IOException {
		Album albumCopiedTo = DestinationAlbumList.getSelectionModel().getSelectedItem();
		if (copiedNotMoved ==1) {
			albumCopiedFrom.addPhoto(copiedPhoto);
		}
		if (albumCopiedFrom.getName().equals(albumCopiedTo.getName()) && copiedNotMoved==1) {

		}
		else {
			for (Photo p : albumCopiedTo.getPhotos()) {
				if (p.getImage().getImageFile().equals(copiedPhoto.getImage().getImageFile())) {
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

			albumCopiedTo.addPhoto(copiedPhoto);
		}

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/IndividualAlbumDisplay.fxml"));
		loader.setController(new individualAlbumController());
		Parent root = loader.load();
		Scene scene = new Scene(root);
		individualAlbumController controller = loader.getController();
		controller.userList = userList;
		controller.currUser=currUser;
		controller.currentAlbumOpen=albumCopiedFrom;
		controller.start(primaryStage);
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();

	}
	/**
	 * This method cancels the photo copying action and brings the user back to the album display
	 * @throws	IOException 
	 * @param	event
	 */
	public void CancelMoveCopyButtonHandler (ActionEvent event) throws IOException {

		albumCopiedFrom.addPhoto(copiedPhoto);


		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/IndividualAlbumDisplay.fxml"));
		loader.setController(new individualAlbumController());
		Parent root = loader.load();
		Scene scene = new Scene(root);
		individualAlbumController controller = loader.getController();
		controller.userList = userList;
		controller.currentAlbumOpen=albumCopiedFrom;
		controller.currUser=currUser;
		controller.start(primaryStage);
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
