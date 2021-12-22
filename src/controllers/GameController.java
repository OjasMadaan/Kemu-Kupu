package controllers;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import application.Festival;
import application.Main;
import application.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.AudioClip;

/**
 * 
 * Parent class to PracticeController and QuizController. Removes duplicate code
 *
 */

public abstract class GameController {
	@FXML
	protected Button enterButton, speakButton, exitButton, macronButton, dontKnowButton, macronHelp,infoButton;
	@FXML
	protected TextField textField;
	@FXML
	protected Label wordNumLabel;
	@FXML
	public Label hintLabel;
	@FXML
	protected Slider ttsSlider;

	// Initialise tooltips
	protected Tooltip toolTip = new Tooltip();
	protected Tooltip infoTool = new Tooltip("Hover over items for more information");

	protected List<String> wordsToTest;
	protected int currentIndex = 0;
	protected int attempts = 1;

	// Importing audio chimes
	protected AudioClip correctChime = new AudioClip(getClass().getResource("/soundFX/correct.mp3").toExternalForm());
	protected AudioClip incorrectChime = new AudioClip(getClass().getResource("/soundFX/incorrect.mp3").toExternalForm());
	protected AudioClip skipChime = new AudioClip(getClass().getResource("/soundFX/skip.mp3").toExternalForm());

	AtomicInteger caretPos = new AtomicInteger();

	User currentUser = Main.userManager.currentUser;


	// function that finds the position of the users cursor/caret to use when adding a macron
	@FXML
	public void readCaretPos() {
		textField.caretPositionProperty().addListener((obs, oldVal, newVal) -> {
			if (textField.isFocused()) {
				caretPos.set(newVal.intValue());
			}
		});
	}

	// Initialise both gif tooltips
	protected void initialiseToolTips(){
		setupMacronToolTip();
		setupInfoToolTip();
	}

	// function allowing user to press enter to submit their answer	
	@FXML
	public void handleEnterKeyPressed(KeyEvent event) throws IOException, InterruptedException {
		// get the position of the caret/cursor every time a key is pressed
		readCaretPos();

		//if the user hit enter then submit 
		if (event.getCode() == KeyCode.ENTER) {
			readInputAndSubmit();
		}
	}

	// function allowing user to click enter button to submit answer
	@FXML
	public void handleEnterButtonClicked(ActionEvent event) throws IOException, InterruptedException {
		readInputAndSubmit();
	}


	public void readInputAndSubmit() throws IOException, InterruptedException {
		if (!textField.getText().isBlank()) { // Check if there is any input in textfield
			textField.setPromptText("Spell Word Here...");
			String input = textField.getText().trim(); // Trim leading and trailing whitespace
			textField.clear();
			submitAnswer(input);
		} else { // Error message if nothing was entered
			textField.setPromptText("CANNOT BE EMPTY");
		}
	}

	public void submitAnswer(String userInput) throws IOException, InterruptedException {
		// overwritten by child classes
	}

	// function that adds a macron to a vowel the user has the caret/cursor on
	@FXML
	public void addMacron(ActionEvent event) {
		if (!textField.getText().isBlank()) { // Make sure user has input
			StringBuilder sb = new StringBuilder(textField.getText());
			int caretIndex = caretPos.intValue()-1;
			if(caretIndex<0){
				return; // Do nothing if users caret isnt infront of any letter
			}
			if (isValidForMacron(sb, caretIndex)){
				Character c = macronised(sb.charAt(caretIndex)); // change vowel to be macronised if possible
				sb.replace(caretIndex, caretIndex+1, c.toString());
				textField.setText(sb.toString()); // update textfield with macron
			}
			focusTextfield();
		}
	}
	// Function to show macron tooltip gif
	@FXML
	public void showMacronHelp(ActionEvent event)  {
		if(toolTip.isShowing()){
			toolTip.hide(); // Hide the tooltip on click if currently showing
		}	else{ // Show the tooltip on click if currently hidden
			toolTip.show(Main.primaryStage, Main.primaryStage.getX()+macronHelp.getLayoutX()+40,Main.primaryStage.getY()+macronHelp.getLayoutY()+70);
		}
	}

	// function to return the appropriate macronised character
	private Character macronised(Character c) {
		Character ch = Character.toLowerCase(c);

		switch (ch) {
		case 'a':
			if (Character.isUpperCase(c)) {
				return '\u0100';
			} else {
				return '\u0101';
			}
		case 'e':
			if (Character.isUpperCase(c)) {
				return '\u0112';
			} else {
				return '\u0113';
			}
		case 'i':
			if (Character.isUpperCase(c)) {
				return '\u012A';
			} else {
				return '\u012B';
			}	
		case 'o':
			if (Character.isUpperCase(c)) {
				return '\u014C';
			} else {
				return '\u014D';
			}
		case 'u':
			if (Character.isUpperCase(c)) {
				return '\u016A';
			} else {
				return '\u016B';
			}
		}
		return null;
	}

	// function checking whether character can be macronised
	protected boolean isValidForMacron(StringBuilder sb, int caretIndex) {
		char c = Character.toLowerCase(sb.charAt(caretIndex));

		Boolean isVowel = (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u');

		if (caretIndex+1 < sb.length() && Character.isLetter(sb.charAt(caretIndex+1))) {
			return isVowel;
		} else if (caretIndex == sb.length()-1) {
			return isVowel;
		}
		return false;
	}

	// function providing functionality for the repeat word button
	@FXML
	protected void repeatWord(ActionEvent event) throws IOException, InterruptedException{
		disableButtons(true);
		Thread.sleep(200);
		Festival.ttsMaori(getSliderValue(), wordsToTest.get(currentIndex), false);
		focusTextfield();
		buttonDebounce();
	}

	// this method prevents users from spamming buttons.
	protected void buttonDebounce() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				disableButtons(false);
			}
		},100);
	}

	// getter function allowing the slider value to be used for festival
	@FXML
	protected double getSliderValue(){
		return Math.round((2-ttsSlider.getValue())*100.0)/100.0;
	}

	protected void disableButtons(Boolean b) {
		// overwritten in child classes
	}

	@FXML
	public void mainMenu(ActionEvent event) throws IOException {
		Main.sceneSwitcher.loadMenuScene();
	}
	// Function used to focus back onto textfield after action and at end of word
	protected void focusTextfield() {
		textField.requestFocus();
		textField.selectEnd();
	}

	// Update user statistic for all-time "correct"
	protected void correct() throws IOException {
		if (currentUser != null) currentUser.correct();
	}

	// Update user statistic for all-time "attempted"
	protected void attempt() throws IOException  {
		if (currentUser != null) currentUser.attempted();
	}
	// initialise info tooltip with gif
	protected void setupInfoToolTip() {
		Image infoImage = new Image(getClass().getResourceAsStream("/images/info.gif"));
		ImageView infoGif = new ImageView(infoImage);
		infoGif.setFitHeight(300);
		infoGif.setPreserveRatio(true);
		infoTool.setStyle("-fx-font-size: 25;");
		infoTool.setContentDisplay(ContentDisplay.BOTTOM);
		infoTool.setGraphic(infoGif);
	}

	// Function that is called to show and hide the information tooltip
	@FXML
	public void showInfo(ActionEvent event)  {
		if(infoTool.isShowing()){
			infoTool.hide(); // hide tool tip on click if showing
		}	else{ // show tooltip on click if hidden
			infoTool.show(Main.primaryStage, Main.primaryStage.getX()+infoButton.getLayoutX()+40,Main.primaryStage.getY()+infoButton.getLayoutY()+80);
		}
	}

	// Function to set our macron tooltip gif
	protected void setupMacronToolTip() {
		// Import gif
		Image macronImage = new Image(getClass().getResourceAsStream("/images/macron.gif"));
		ImageView macronGif = new ImageView(macronImage); // Create image with gif
		macronGif.setFitHeight(210);
		macronGif.setPreserveRatio(true); //height of tool tip
		toolTip.setGraphic(macronGif);
	}
}