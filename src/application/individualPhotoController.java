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
import javafx.scene.control.ListView;

import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import objects.*;

/**
 * This class functions as the backend for the individual photo window
 */
public class individualPhotoController {
	/**
	 * This button allows the user to return to their profile
	 */
	@FXML
	Button BackOutPhotoViewButton;
	/**
	 * This button allows the user to change their photo's caption
	 */
	@FXML
	Button EditCaptionButton;
	/**
	 * This button allows the user to enter a tag for their photo
	 */
	@FXML
	Button AddTagButton;
	/**
	 * This button allows the user to edit a tag for their photo
	 */
	@FXML 
	Button EditTagButton;
	/**
	 * This button allows the user to delete a tag for their photo
	 */
	@FXML
	Button DeleteTagButton;
	/**
	 * A textfield for the user to enter their new tag's key
	 */
	@FXML
	TextField TagTextField;
	/**
	 * A textfield for the user to enter their new tag's value
	 */
	@FXML
	TextField ValueTextField;
	/**
	 * A textfield for the user to add a caption
	 */
	@FXML
	TextField CaptionTextField;
	/**
	 * Displays the date and time a photo was taken
	 */
	@FXML
	TextField DateandTimeofCaptureTextField;

	/**
	 * A ListView to display tags
	 */
	@FXML
	ListView<Tag> tagListView;
	/**
	 * An ImageView to display the selected image
	 */
	@FXML
	ImageView IndividualPhotoImageView;

	/**
	 * An ObsList to store an ArrayList of tags
	 */
	ObservableList<Tag> obsList; 
	/**
	 * The current photo album
	 */
	Album currAlbum;
	/**
	 * The current photo
	 */
	Photo currPhoto;
	/**
	 * The current user
	 */
	User currUser;
	/**
	 * A list of all users
	 */
	userList userList;
	/**
	 * The controller's stage
	 */
	Stage primaryStage;
	/**
	 * An integer to keep track of what the previous window was
	 */

	int prevWindow;

	/**
	 * This method launches the individual photo window
	 * @param	mainstage
	 */

	public void start (Stage mainstage) {
		System.out.println(currPhoto.getImage().getImageFile());
		File file = currPhoto.getImage().getImageFile();
		Image image = new Image(file.toURI().toString());        
		IndividualPhotoImageView.setImage(image);

		ArrayList<Tag> tags = currPhoto.getTags();
		Collections.sort(tags);

		primaryStage=mainstage;		
		obsList = FXCollections.observableArrayList(currPhoto.getTags());	
		tagListView.setItems(obsList);
		CaptionTextField.setText(currPhoto.getCaption());
		DateandTimeofCaptureTextField.setText(currPhoto.getImage().getDate().toString());


	}
	/**
	 * This method allows the user to delete a tag
	 * @param	event
	 */
	public void DeleteTagButtonHandler (ActionEvent event) {
		if (obsList.size()==0) {
			noSelection();
			return;
		}
		Tag tagToDelete = tagListView.getSelectionModel().getSelectedItem();

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText("Confirm deletion");
		alert.setContentText("Are you sure you want to delete this tag?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			obsList.remove(tagToDelete);
			currPhoto.removeTag(tagToDelete);			
		}		

	}
	/**
	 * This method allows the user to edit a tag
	 * @param	event
	 */
	public void EditTagButtonHandler(ActionEvent event) {
		if (obsList.size()==0) {
			noSelection();
			return;
		}
		String newTag= TagTextField.getText();
		String newValue=ValueTextField.getText();
		
		if (newTag.length()==0 || newValue.length()==0) {
			missingFields();
			return;
		}
		
		Tag tag = tagListView.getSelectionModel().getSelectedItem();
		if (tag==null) {
			Alert alert = 
					new Alert(AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText("No selection");
			String content = "Make a selection first";
			alert.setContentText(content);
			alert.showAndWait();
			return;
		}
		tag.setKey(TagTextField.getText());
		tag.setValue(ValueTextField.getText());
		obsList.clear();
		Collections.sort(currPhoto.getTags());
		obsList.addAll(currPhoto.getTags());
		TagTextField.setText("");
		ValueTextField.setText("");



	}
	/**
	 * This method allows the user to edit a tag
	 * @param	event
	 */
	public void EditCaptionButtonHandler (ActionEvent event)  {

		String newCaption = CaptionTextField.getText();
		currPhoto.setCaption(newCaption); 
		CaptionTextField.setText(newCaption);


	}
	/**
	 * This method allows the user to add a tag
	 * @param	event
	 */
	public void AddTagButtonHandler (ActionEvent event) {

		String newType = TagTextField.getText();
		String newValue = ValueTextField.getText();
		if (newType.length()==0 || newValue.length()==0) {
			missingFields();
			return;
		}
		Tag newTag = new Tag (newType, newValue);
		currPhoto.addTag(newTag);
		obsList.clear();
		Collections.sort(currPhoto.getTags());
		obsList.addAll(currPhoto.getTags());
		TagTextField.setText("");
		ValueTextField.setText("");

	}
	/**
	 * This method determines what the previous window was
	 * @throws	IOException if the selected fxml file can't be found
	 * @param	event
	 */

	public void BackOutPhotoViewButtonHandler (ActionEvent event) throws IOException {
		if (prevWindow==1) {
			returnToAlbumView();
		}
		if (prevWindow==2) {
			returnToSearchResults();
		}
	}
	/**
	 * This method brings the user back to the individual album view
	 * @throws	IOException if the album view's fxml file can't be found
	 */
	public void returnToAlbumView() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/IndividualAlbumDisplay.fxml"));
		loader.setController(new individualAlbumController());
		Parent root = loader.load();
		Scene scene = new Scene(root);
		individualAlbumController controller = loader.getController();
		controller.currentAlbumOpen=currAlbum;
		controller.userList = userList;
		controller.currUser=currUser;
		controller.start(primaryStage);
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	/**
	 * This method brings the user back to the search results window
	 * @throws	IOException if the album view's fxml file can't be found 
	 */
	public void returnToSearchResults() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SearchResults.fxml"));
		loader.setController(new searchResultsController());
		Parent root = loader.load();
		Scene scene = new Scene(root);
		searchResultsController controller = loader.getController();
		controller.userList = userList;
		controller.currUser=currUser;
		controller.currAlbum=currAlbum;
		controller.start(primaryStage);
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	/**
	 * This method tells the user when they haven't filled out the appropriate fields
	 */
	
	public void missingFields() {
		Alert alert = 
				new Alert(AlertType.INFORMATION);
		alert.setTitle("Error");
		alert.setHeaderText("Missing fields");
		String content = "Fill out all the required fields";
		alert.setContentText(content);
		alert.showAndWait();
	}
	
	public void noSelection() {
		Alert alert = 
				new Alert(AlertType.INFORMATION);
		alert.setTitle("Error");
		alert.setHeaderText("No selection");
		String content = "Make a selection first";
		alert.setContentText(content);
		alert.showAndWait();
		return;
	}

}

