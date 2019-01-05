package application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import objects.*;

/**
 * This class functions as the backend for the login window
 */

public class loginController {
	/**
	 * allows the user to enter their username
	 */
	@FXML
	TextField usernameField;
	/**
	 * Launches the application if username matches the list of users in the system
	 */
	@FXML
	Button enterBtn;
	/**
	 * quits the application
	 */
	@FXML
	Button logoutBtn;
	
	/**
	 * the login controller's stage
	 */

	Stage primaryStage;
	
	/**
	 * a list of users in the system
	 */

	userList userList;
	
	/**
	 * the user logging in
	 */

	User currUser;

	/**
	 * This method launches the login window
	 * @param	stage
	 */

	public void start(Stage stage)  {  

		primaryStage=stage;

	}
	/**
	 * This method opens the appropriate subsystem: admin if the username entered it "admin", regular user otherwise. If the name entered 
	 * does not match any in the system, the user is alerted that they entered an incorrect name
	 * @throws	IOException if the fxml address can't be found

	 * @throws	ClassNotFoundException if the fxml address can't be found
	 * @param	event
	 */ 
	public void handleEnterBtn(ActionEvent event) throws IOException, ClassNotFoundException {

		String user = usernameField.getText();
		
		userList = userList.readUsers();

		User currLogin = userList.isUserInList(user);


		if (user == null || user.equals("")) {
			noSuchUser();
		}
		if (user.equals("admin")) {
			exitWindowController exitCtrl = new exitWindowController();
			exitCtrl.userList = userList;

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
			return;
		}
		if (user.toLowerCase().equals("stock")&& userList.isUserInList("stock")==null && !userList.hasStockBeenDeleted()) {
			
			loadStockPhotos();
			return;
		} 
		if (currLogin != null) {
			exitWindowController exitCtrl = new exitWindowController();
			exitCtrl.userList = userList;

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/NonAdminUserProfile.fxml"));
			loader.setController(new nonAdminProfileController());
			Parent root = loader.load();
			Scene scene = new Scene(root);
			nonAdminProfileController controller = loader.getController();
			controller.userList = userList;
			controller.currUser = currLogin;
			controller.start(primaryStage);
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.show();
			return;	
		}
		
		else { //tell the user they entered the wrong name
			noSuchUser(); 
		}

	}
	public void loadStockPhotos() throws IOException {
		User stock = new User("stock");
		userList.addUser(stock);
		Album stockAlbum = new Album("stock");
		  File imageFile1 = new File("photo1.jpg");
          photoImage newImage1 = new photoImage(imageFile1);
          Photo photo1 = new Photo(newImage1, null);
          stockAlbum.addPhoto(photo1);
          
          File imageFile2 = new File("photo2.jpg");
          photoImage newImage2 = new photoImage(imageFile2);
          Photo photo2 = new Photo(newImage2, null);
          stockAlbum.addPhoto(photo2);
          
          File imageFile3 = new File("photo3.jpg");
          photoImage newImage3 = new photoImage(imageFile3);
          Photo photo3 = new Photo(newImage3, null);
          stockAlbum.addPhoto(photo3);
          
          File imageFile4 = new File("photo4.jpg");
          photoImage newImage4 = new photoImage(imageFile4);
          Photo photo4 = new Photo(newImage4, null);
          stockAlbum.addPhoto(photo4);
          
          File imageFile5 = new File("photo5.jpg");
          photoImage newImage5 = new photoImage(imageFile5);
          Photo photo5 = new Photo(newImage5, null);
          stockAlbum.addPhoto(photo5);
          
          stock.addAlbum(stockAlbum);
   
          
          
          exitWindowController exitCtrl = new exitWindowController();
			exitCtrl.userList = userList;

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/NonAdminUserProfile.fxml"));
			loader.setController(new nonAdminProfileController());
			Parent root = loader.load();
			Scene scene = new Scene(root);
			nonAdminProfileController controller = loader.getController();
			controller.userList = userList;
			controller.currUser = stock;
			controller.start(primaryStage);
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.show();
			return;	
          
	}
	/**
	 * This method quits the application.
	 * @throws IOException if the location to write the userlist to can't be created/found
	 * @param	event
	 */
	public void handlelogoutBtn (ActionEvent event) throws IOException {
		userList.writeUsers(userList);

		System.exit(0);
		
	}
	/**
	 * This method informs the user that they've entered an incorrect username.
	 */

	private void noSuchUser () { //for when a user isn't in the list
		Alert alert = 
				new Alert(AlertType.INFORMATION);
		alert.setTitle("Error");
		alert.setHeaderText("No such user");
		String content = "There isn't a user with that name";
		alert.setContentText(content);
		alert.showAndWait();

	}

}



