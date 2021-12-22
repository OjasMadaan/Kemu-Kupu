package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Festival;
import application.Main;

/**
 * 
 * Class to control the functionality for the practice mode
 *
 */

public class PracticeController extends GameController implements Initializable {
	@FXML
	Button finishPracticeButton;
	@FXML
	Label attemptedLabel;
	@FXML
	Label correctLabel;
	@FXML
	Button advanceButton;	

	int numWords;
	int numCorrect;
	int attempted;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		initialiseToolTips();
	}

	// checks if users answer is correct
	public void submitAnswer(String userInput) throws IOException, InterruptedException {
		attempted++;
		attempt();

		// if user gets word right first try
		if(userInput.equalsIgnoreCase(wordsToTest.get(currentIndex))){ // case for correct
			correctChime.play();
			correct();
			numCorrect++;
			goNextWord();
			attempts=1;

			// if user gets word wrong first try
		} else if(attempts == 1){
			attempts = 2;
			incorrectChime.play();
			Festival.ttsMaori(getSliderValue(),wordsToTest.get(currentIndex), false);
			showHint();
			textField.requestFocus();
			textField.selectEnd();
			// if user gets both attempts wrong
		} else if(attempts ==2){
			incorrectChime.play();
			showWordAndWait();
		}
	}

	// Disable and enable buttons to prevent spam during festival speaking
	protected void disableButtons(Boolean b) {
		enterButton.setDisable(b);
		macronButton.setDisable(b);
		speakButton.setDisable(b);
	}

	// Allow user to move on after seeing correct answer
	public void advanceButtonClicked(ActionEvent e) throws IOException, InterruptedException {
		disableButtons(true); // disable other buttons
		goNextWord();
		attempts = 1;
		advanceButton.setVisible(false);
		buttonDebounce(); // prevent button spam
		disableButtons(false); // enable buttons
	}

	// Function shows correct spelling and allows user to study it before advancing
	public void showWordAndWait() {
		disableButtons(true);
		char[] wordArray = wordsToTest.get(currentIndex).toCharArray();
		StringBuilder word = new StringBuilder();
		for(char c: wordArray){
			word.append(c+" ");
		}
		hintLabel.setText(word.toString());
		advanceButton.setVisible(true);
		advanceButton.requestFocus();
	}
	// function to show user a hint after getting the first attempt incorrect
	public void showHint() {
		String currentWord = wordsToTest.get(currentIndex);
		char[] wordArray = currentWord.toCharArray();
		char[] hintArray = hintLabel.getText().toCharArray();
		// Show every second letter of a word as a hint
		for(int i = 1; i < wordArray.length; i+=2) {
			hintArray[2*i] = wordArray[i];
		}
		hintLabel.setText(String.valueOf(hintArray)); // update label with hint
		hintLabel.setVisible(true);
	}

	@FXML
	public void goToSummary(ActionEvent event) throws IOException {
		Main.sceneSwitcher.loadSummaryScene(wordsToTest, numCorrect, attempted);
	}

	// function that moves from the current word to the next word
	public void goNextWord() throws IOException, InterruptedException {
		textField.clear();
		currentIndex += 1;
		attemptedLabel.setText("Attempted = " + attempted);
		correctLabel.setText("Correct = " + numCorrect);
		if(currentIndex<numWords) {
			String currentWord = wordsToTest.get(currentIndex);
			Festival.ttsEnglish(1,"Now spell");
			Festival.ttsMaori(getSliderValue(), currentWord, false);
			attempts = 1;
			hintLabel.setText(toUnderscores(currentWord));
		} else{ // move to summary screen with relevant information needed to display
			Main.sceneSwitcher.loadSummaryScene(wordsToTest, numCorrect, currentIndex);
		}
		focusTextfield();
	}

	// setter function allowing the tested words to be passed into the controller class
	public void setWords(List<String> wordList) throws IOException, InterruptedException {
		this.wordsToTest = wordList;
		this.numWords = wordList.size();
	}
	// Convert word to the equivalent amount of underscores
	public String toUnderscores(String word) {
		char[] characters = word.toCharArray();
		StringBuilder hiddenWord = new StringBuilder();
		for(char c : characters){
			if (Character.isLetter(c)) {
				hiddenWord.append("_ ");
			} else {
				hiddenWord.append("  ");
			}
		}	
		return hiddenWord.toString();
	}
}