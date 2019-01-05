package application;

import java.io.File;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import objects.*;

/**
 * This class functions as the backend for the album slideshow window
 */

public class albumSlideshowController {
	/**
	 * displays the current photo
	 */
	@FXML
	ImageView SlideshowImageView;
	
	/**
	 * allows the user to return to the previous photo
	 */
	@FXML
	Button PreviousImageBtn;
	/**
	 * allows the user to advance to the next photo
	 */
	@FXML
	Button NextImageBtn;
	/**
	 * brings the user back to the album view
	 */
	@FXML
	Button returnToAlbumBtn;
	/**
	 * the window's stage
	 */
	Stage primaryStage;
	/**
	 * A list of all the users
	 */
	userList userList;
	/**
	 * This is the current user
	 */
	User currUser;
	/**
	 * This is the current album open
	 */
	
	Album currAlbum;
	/**
	 * This integer keeps track of what number slide the user is looking at
	 */
	int currSlide=0;
	/**
	 * This integer keeps track of the current album's size
	 */
	int albumSize;
	/**
	 * This integer keeps track of what window was previously open
	 */
	int prevWindow;
	
	/**
	 * This method launches the exit window
	 * @param	mainstage
	 */

	public void start (Stage mainstage) {
		primaryStage=mainstage;
		albumSize= currAlbum.getPhotos().size();
		
		Photo currPhoto = currAlbum.getPhotos().get(0);
		
		File file = currPhoto.getImage().getImageFile();
		Image image = new Image(file.toURI().toString());        
		SlideshowImageView.setImage(image);
		

	}
	/**
	 * This method shows the next photo
	 * @param	event
	 */
	public void NextImageBtnHandler (ActionEvent event) {
		currSlide++;
		if (currSlide == albumSize) {
			Photo currPhoto = currAlbum.getPhotos().get(0);
			
			File file = currPhoto.getImage().getImageFile();
			Image image = new Image(file.toURI().toString());        
			SlideshowImageView.setImage(image);
			currSlide=0;
			return;	
		}
		Photo currPhoto = currAlbum.getPhotos().get(currSlide);
		
		File file = currPhoto.getImage().getImageFile();
		Image image = new Image(file.toURI().toString());        
		SlideshowImageView.setImage(image);
	}
	/**
	 * This method shows the previous image
	 * @param	event
	 */
	public void PreviousImageBtnHandler (ActionEvent event) {
		currSlide--;
		if (currSlide==-1) {
			Photo currPhoto = currAlbum.getPhotos().get(albumSize-1);
			File file = currPhoto.getImage().getImageFile();
			Image image = new Image(file.toURI().toString());        
			SlideshowImageView.setImage(image);
			currSlide=albumSize-1;
			return;	
		}
		Photo currPhoto = currAlbum.getPhotos().get(currSlide);
		
		File file = currPhoto.getImage().getImageFile();
		Image image = new Image(file.toURI().toString());        
		SlideshowImageView.setImage(image);
	}	
	/**
	 * This method returns to the previous album, whether it be a search compilation album or a user-created one
	 * @throws	IOException if the location to write the search results windowor album window fxml can't be found
	 * @param	event
	 */
	public void returnToAlbumBtnHandler (ActionEvent event) throws IOException {
		if (prevWindow==1) {
			returnToAlbum();
			return;
		}
		if (prevWindow==2) {
			returnToSearch();
		}
	}
	/**
	 * This method brings the user back to the album window
	 * @throws	IOException if the album window's fxml file can't be found
	 */
	public void returnToAlbum() throws IOException {
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
	 * @throws	IOException if the search results window's fxml file can't be found
	 */
	public void returnToSearch() throws IOException{
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
	
}
