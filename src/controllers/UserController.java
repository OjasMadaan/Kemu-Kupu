package controllers;

import java.io.IOException;

import application.Main;
import application.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

/**
 * 
 * This class provides functionality to the user screen
 *
 */
public class UserController {

	@FXML
	Button maleIcon, tikiIcon, femaleIcon;
	@FXML
	public Button returningUserButton;
	@FXML
	TextField usernameField;
	@FXML
	Label promptLabel, usernameLabel;
	@FXML
	Label allTimeAttempted, allTimeCorrect, allTimeAccuracy;
	@FXML
	ImageView userIcon;

	private int numUsers = Main.userManager.getUsers().size();
	public int userIndex = 0;
	private String icon = null;
	
	/**
	 * This method gets a deletion confirmation and then
	 * removes the current user from the user-manager object
	 * 
	 * @param event
	 * @throws IOException
	 */
	public void deleteUser(ActionEvent event) throws IOException {
		String currentUserName = Main.userManager.currentUser.getName();
		if (deleteUserConfirm()) {
			Main.userManager.removeUser(currentUserName);
			Main.sceneSwitcher.loadOpeningScene();
		}
	}

	
	/**
	 * This method shows the user deletion confirmation dialog, 
	 * gets the result then returns it.
	 * 
	 * @return boolean true/false
	 */
	public boolean deleteUserConfirm() {
		User currentUser = Main.userManager.currentUser;
		// configure alert box.
		Alert alert = setupAlert(currentUser);
		
		// get result from alert box
		alert.showAndWait();
		if (alert.getResult() == ButtonType.OK) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Shows the next user from list of users.
	 * If we reach the end of the list, we wrap around to the first user.
	 * 
	 * @param event
	 */
	@FXML
	public void showNextUser(ActionEvent event) {
		if (userIndex == numUsers-1) {
			userIndex = 0;
		} else {
			userIndex++;
		}
		showUser();
	}

	/**
	 * Shows the previous user from list of users.
	 * If the index goes below 0, then we wrap around the list.
	 * 
	 * @param event
	 */
	@FXML
	public void showPrevUser(ActionEvent event) {
		if (userIndex == 0) {
			userIndex = numUsers-1;
		} else {
			userIndex--;
		}
		showUser();
	}

	/**
	 * This method displays the user and their icon in the returning user menu.
	 */
	public void showUser() {
		User currentUser = Main.userManager.getUsers().get(userIndex);
		Main.userManager.currentUser = currentUser;
		userIcon.setImage(currentUser.getIcon());
		usernameLabel.setText(currentUser.getName());
	}

	/**
	 * This method sets the selected icons background colour to green
	 * and updates this controllers user icon field.
	 * 
	 * @param event (button)
	 */
	@FXML
	public void selectIcon(ActionEvent event){
		// remove colour from buttons which aren't selected.
		maleIcon.setStyle(null);
		femaleIcon.setStyle(null);
		tikiIcon.setStyle(null);
		// colour selected icons button
		((Button) event.getSource()).setStyle("-fx-background-color: rgb(167, 207, 180);");	
		this.icon = ((Button) event.getSource()).getId();
	}

	/**
	 * This method performs the appropriate checks before creating
	 * the user with the selected username and icon. 
	 * Once the user is created it loads up the main menu screen.
	 * 
	 * @param event (button)
	 * @throws IOException
	 */
	@FXML
	public void createUser(ActionEvent event) throws IOException {
		// check if username field is empty or no user icon is selected
		if (usernameField.getText().isEmpty()) {
			usernameField.setPromptText("CANNOT BE EMPTY");
		} else if (this.icon == null) {
			promptLabel.setVisible(true);
			promptLabel.setText("Please select a user icon");
		} 
		
		if (!usernameField.getText().isEmpty() && icon != null) {
			String username = usernameField.getText().trim();
			
			// check if username is already taken
			if (Main.userManager.getUserMap().containsKey(username)) {
				promptLabel.setVisible(true);
				promptLabel.setText("Username taken, try a different one");
			} else {
				Main.userManager.addUser(username, icon);
				Main.sceneSwitcher.loadMenuScene();
			}

		} 
	}

	/**
	 * Display the applications current users statistics, username and icon
	 */
	public void showProfileView() {
		User currentUser = Main.userManager.currentUser;
		allTimeAttempted.setText("Attempted : " + currentUser.getAttempted());
		allTimeCorrect.setText("Correct : " + currentUser.getCorrect());
		allTimeAccuracy.setText("Accuracy : " + currentUser.getAccuracy() + " %");
		userIcon.setImage(currentUser.getIcon());
		usernameLabel.setText(currentUser.getName());
	}
	
	
	/**
	 * This method sets up the alert dialog to confirm deletion of a user
	 * 
	 * @param currentUser
	 * @return Alert which asks for confirmation
	 */
	public Alert setupAlert(User currentUser) {
		// configure alert box.
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete User?");
		alert.setHeaderText("The user '" + currentUser.getName() + "' will be deleted.");
		
		// add styling to match theme
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(getClass().getResource("alert.css").toExternalForm());
		
		// add user icon to alert
		userIcon = new ImageView(currentUser.getIcon());
		userIcon.setPreserveRatio(true);
		userIcon.setFitHeight(80);
		alert.setGraphic(userIcon);
		
		return alert;
	}
	
	////**** Various methods for navigating between scenes ****////
	
	public void guestMode(ActionEvent e) throws IOException{
		// set current user to null to indicate guest mode
		Main.userManager.currentUser = null;
		Main.sceneSwitcher.loadMenuScene();
	}

	public void newUser(ActionEvent e) throws IOException {
		Main.sceneSwitcher.loadCreateUserScene();
	}

	public void returningUser(ActionEvent e) throws IOException {
		Main.sceneSwitcher.loadReturningUserScene();
	}

	public void selectReturningUser(ActionEvent e) throws IOException {
		Main.sceneSwitcher.loadMenuScene();
	}
	
	public void exitProfileView(ActionEvent e) throws IOException {
		Main.sceneSwitcher.loadMenuScene();
	}

	public void backButton(ActionEvent e) throws IOException {
		Main.sceneSwitcher.loadOpeningScene();
	}
}
