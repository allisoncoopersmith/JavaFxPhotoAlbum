package application;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

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
import javafx.stage.Stage;
import objects.*;


public class searchEngineController {
	/**
	 * This DatePicker allows the user to select a start date for searches
	 */
	@FXML
	DatePicker startDatePicker;
	/**
	 * This DatePicker allows the user to select an end date for searches
	 */
	@FXML
	DatePicker endDatePicker;
	/**
	 * This Button allows the user to return to the previous page
	 */
	@FXML
	Button CancelSearchEngineButton;
	/**
	 * This TextField allows the user to enter a first tag search term
	 */
	@FXML 
	TextField Tag1TextField;
	/**
	 * This TextField allows the user to enter a first value search term
	 */
	@FXML
	TextField Value1TextField;
	/**
	 * This TextField allows the user to enter a second tag search term
	 */
	@FXML
	TextField Tag2TextField;
	/**
	 * This TextField allows the user to enter a second value search term
	 */
	@FXML
	TextField Value2TextField;
	/**
	 * This Button allows the user to return to search by a date
	 */
	@FXML
	Button searchDateBtn;
	/**
	 * This Button allows the user to search by a single tag
	 */
	@FXML 
	Button singleTagSearchBtn;
	/**
	 * This Button allows the user to search by a conjunction of search terms
	 */
	@FXML
	Button andBtn;
	/**
	 * This Button allows the user to search by a disjunction of search terms
	 */
	@FXML
	Button orBtn;

	/**
	 * This is the current user
	 */
	User currUser;
	/**
	 * This is a list of all users
	 */
	userList userList;
	/**
	 * This is the window's stage
	 */
	Stage primaryStage;
	

	/**
	 * This method launches the window
	 * @param	mainstage
	 */

	public void start (Stage mainstage) {
		primaryStage=mainstage;
	}
	/**
	 * This method controls the or button
	 * @param	event
	 * @throws	IOException if the specified fxml file can't be found
	 */
	public void orBtnHandler (ActionEvent event) throws IOException {
		ArrayList<Photo> allPhotos = getAllPhotos();
		ArrayList<Photo> result = new ArrayList<Photo>();

		if (allPhotos.size()==0) {
			noPhotos();
			return;		
		}
		String key1 = Tag1TextField.getText();
		String key2 = Tag2TextField.getText();
		String value1 = Value1TextField.getText();
		String value2 = Value2TextField.getText();


		if (key1.length()==0 || value1.length()==0 || key2.length()==0 || value2.length()==0) {
			missingFields();
			return;
		}
		for (Photo p: allPhotos) {
			ArrayList<Tag> tags = p.getTags();
			for (Tag t: tags) {
				if ((t.getKey().equals(key1) && t.getValue().equals(value1))||(t.getKey().equals(key2) && t.getValue().equals(value2))) {
					if (checkForDuplicates(p, result)) {
						result.add(p);
					}
				}
			}
		}
		if (result.size()==0) {
			noPhotosMatching();
			return;
		}
		openResults(result);
	}
	/**
	 * This method controls the and search button
	 * @param	event
	 * @throws	IOException if the specified fxml file can't be found
	 */


	public void andBtnHandler(ActionEvent event) throws IOException {
		ArrayList<Photo> allPhotos = getAllPhotos();
		ArrayList<Photo> intermediate = new ArrayList<Photo>();
		ArrayList<Photo> result = new ArrayList<Photo>();

		if (allPhotos.size()==0) {
			noPhotos();
			return;		
		}
		String key1 = Tag1TextField.getText();
		String key2 = Tag2TextField.getText();
		String value1 = Value1TextField.getText();
		String value2 = Value2TextField.getText();


		if (key1.length()==0 || value1.length()==0 || key2.length()==0 || value2.length()==0) {
			missingFields();
			return;
		}
		for (Photo p: allPhotos) {
			ArrayList<Tag> tags = p.getTags();
			for (Tag t: tags) {
				if (t.getKey().equals(key1) && t.getValue().equals(value1)) {
					intermediate.add(p);
				}
			}
		}
		for (Photo p: intermediate) {
			ArrayList<Tag> tags = p.getTags();
			for (Tag t: tags) {
				if (t.getKey().equals(key2) && t.getValue().equals(value2)) {
					if (checkForDuplicates(p, result)) {
						result.add(p);
					}
				}
			}
		}


		if (result.size()==0) {
			noPhotosMatching();
			return;
		}
		openResults(result);
	}
	/**
	 * This method controls the single tag search button
	 * @param	event
	 * @throws	IOException if the specified fxml file can't be found
	 */


	public void singleTagSearchBtnHandler(ActionEvent event) throws IOException {
		ArrayList<Photo> allPhotos = getAllPhotos();
		ArrayList<Photo> result = new ArrayList<Photo>();
		if (allPhotos.size()==0) {
			noPhotos();
			return;

		}
		String key = Tag1TextField.getText();
		String value = Value1TextField.getText();

		if (key.length()==0 || value.length()==0) {
			missingFields();
			return;
		}
		for (Photo p: allPhotos) {
			ArrayList<Tag> tags = p.getTags();
			for (Tag t: tags) {
				if (t.getKey().equals(key) && t.getValue().equals(value)) {
					if (checkForDuplicates(p, result)) {
						result.add(p);
					}
				}
			}
		}
		if (result.size()==0) {
			noPhotosMatching();
			return;
		}
		openResults(result);


	}
	/**
	 * This method controls the search date button
	 * @param	event
	 * @throws	IOException if the specified fxml file can't be found
	 */
	public void searchDateBtnHandler(ActionEvent event) throws IOException {
		ArrayList<Photo> allPhotos = getAllPhotos();
		ArrayList<Photo> result = new ArrayList<Photo>();
		if (allPhotos.size()==0) {
			noPhotos();
			return;

		}
		
		if (startDatePicker.getValue()==null || endDatePicker.getValue() == null) {
			missingFields();
			return;
		}
		LocalDate localStartDate = startDatePicker.getValue();
		LocalDateTime localEndDate = endDatePicker.getValue().atStartOfDay().plusDays(1).minusSeconds(1);
	    
	

		Date startDate = Date.from(localStartDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date endDate = Date.from(localEndDate.atZone(ZoneId.systemDefault()).toInstant());


		for (Photo p: allPhotos) {
			Date date = p.getImage().getDate();
			if (date.compareTo(startDate)>=0 && date.compareTo(endDate)<=0) {
				if (checkForDuplicates(p, result)) {
					result.add(p);
				}
			}
		} 

		if (result.size()==0) {
			noPhotosMatching();
			return;
		}
		openResults(result);
	}
	/**
	 * This method launches the results window
	 * @param	photos
	 * @throws	IOException if the specified fxml file can't be found
	 */
	public void openResults(ArrayList<Photo> photos) throws IOException {
		Album resultsAlbum = new Album("Results Album");
		resultsAlbum.getPhotos().addAll(photos);

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SearchResults.fxml"));
		loader.setController(new searchResultsController());
		Parent root = loader.load();
		Scene scene = new Scene(root);
		searchResultsController controller = loader.getController();
		controller.userList = userList;
		controller.currUser=currUser;
		controller.currAlbum=resultsAlbum;
		controller.start(primaryStage);
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();


	}
	/**
	 * This method controls the cancel button
	 * @param	event
	 * @throws	IOException if the specified fxml file can't be found
	 */

	public void CancelSearchEngineButtonHandler (ActionEvent event) throws IOException {
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
	 * This method is called if the user doesn't fill out the appropriate fields
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
	/**
	 * This method is called if there are no photos matching the user's terms
	 */

	public void noPhotosMatching() {
		Alert alert = 
				new Alert(AlertType.INFORMATION);
		alert.setTitle("Nothing found");
		alert.setHeaderText("No photos to display");
		String content = "There are no photos matching your search terms.";
		alert.setContentText(content);
		alert.showAndWait();

	}
	/**
	 * This method is called if there are no photos in the user's profile
	 * @throws	IOException if the specified fxml file can't be found
	 */

	public void noPhotos() throws IOException {
		Alert alert = 
				new Alert(AlertType.INFORMATION);
		alert.setTitle("Error");
		alert.setHeaderText("No photos");
		String content = "Upload some photos first";
		alert.setContentText(content);
		alert.showAndWait();

		returnToPreviousScreen();
	}
	/**
	 * This method returns the user to the previous screen
	 * @throws	IOException if the specified fxml file can't be found
	 */
	public void returnToPreviousScreen() throws IOException {
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
	 * This method returns all the user's uploaded photos
	 * @return	allPhotos
	 */
	public ArrayList<Photo> getAllPhotos() {
		ArrayList<Photo> allPhotos = new ArrayList<Photo>();
		ArrayList<Album> albums = currUser.getAlbums();
		for (Album a: albums) {
			ArrayList<Photo> photos= a.getPhotos();
			for (Photo p: photos) {
				allPhotos.add(p);

			}
		}
		return allPhotos;
	}
	/**
	 * This method checks for duplicate photos
	 * @param	p
	 * @param	photos
	 */
	public boolean checkForDuplicates(Photo p, ArrayList<Photo> photos) {
		for (Photo pho: photos) {
			if (pho.getImage().getImageFile().equals(p.getImage().getImageFile())) {
				return false;
			}
		}
		return true;

	}
}