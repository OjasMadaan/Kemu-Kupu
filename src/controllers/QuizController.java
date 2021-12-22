package controllers;

/**Class that controls quiz functionality.
 * Includes:
 * Marking
 * Help
 * Add macron
 * Repeat word
 * **/

import java.io.IOException;
import java.net.URL;
import java.util.*;

import application.Festival;
import application.Main;
import javafx.animation.FillTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class QuizController extends GameController implements Initializable {
	@FXML
	Label currentScore;
	@FXML
	Label multiplier;
	@FXML
	Rectangle multiplierRectangle;

	private static final Integer STARTTIME = 19;
	private Timeline timeline;
	private IntegerProperty timeSeconds = new SimpleIntegerProperty(STARTTIME);

	private LinkedHashMap<String, String> rewardMap;

	double score = 0;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		initialiseToolTips();
		rewardMap = new LinkedHashMap<>();
		multiplier.textProperty().bind((timeSeconds.add(1)).asString());
		startTimer();
	}
    // Function to start/restart score multiplier timer
	private void startTimer(){
		if (timeline != null) {
			timeline.stop();
		}
		timeline = new Timeline();
		timeSeconds.set(STARTTIME);
		timeline.getKeyFrames().add(
				new KeyFrame(Duration.seconds(STARTTIME+1),
						new KeyValue(timeSeconds, 0)));
		timeline.setDelay(Duration.seconds(1));
		timeline.playFromStart();
		colourMultiplier();
	}

	/**
    * Function marks the user input and determines if it is correct or not.
    * Function also handles scoring
    *
    * @param String user input
    *
    */
	public void submitAnswer(String userInput) throws IOException, InterruptedException {
		attempt();

		rewardMap.put(wordsToTest.get(currentIndex), "incorrect"); // default for word in map
		// if user gets word right first try
		if(userInput.equalsIgnoreCase(wordsToTest.get(currentIndex))){ // case for correct
			if (attempts == 1) {
				score +=1*(timeSeconds.get()+1);

				updateScore();
			} else {
				score +=0.5*(timeSeconds.get()+1);
				updateScore();
			}

			rewardMap.put(wordsToTest.get(currentIndex), "correct");
			correct();
			correctChime.play();

			goNextWord();
			attempts=1;
			// if user gets word wrong first try
		} else if(attempts == 1){
			attempts = 2;
			incorrectChime.play();
			startTimer();
			Festival.ttsMaori(getSliderValue(),wordsToTest.get(currentIndex), false);
			showHint();
			focusTextfield();
			// if user gets both attempts wrong
		} else if(attempts ==2){
			incorrectChime.play();
			goNextWord();
			attempts =1;
		}
	}

	// function to show user a hint after getting the first attempt incorrect
	public void showHint() {
		// get old text from hint label (just underscores)
		char[] hint = hintLabel.getText().toCharArray();

		// update hint label, with 2nd letter shown
		hint[2] = wordsToTest.get(currentIndex).charAt(1);
		hintLabel.setText(String.valueOf(hint));
	}

	/**
    * Function skips to the next word and plays a sound cue
    * also calls functions to prevent button spamming
    *
    * @param event
    * @throws IOException, InterruptedException
    */
	@FXML
	public void dontKnowButtonPressed(ActionEvent event) throws IOException, InterruptedException {
		disableButtons(true);
		attempt();
		rewardMap.put(wordsToTest.get(currentIndex), "skip");
		if (currentIndex != 4) {
			skipChime.play();
		}
		goNextWord();
		focusTextfield();
		buttonDebounce();
	}


	/**
    * Function sets the word to spell to the next word in the list
    * Also calls text to speech functions
    *
    *
    * @throws IOException, InterruptedException
    */
	public void goNextWord() throws IOException, InterruptedException {
		currentIndex += 1;
		textField.clear();
		if(currentIndex<5) {
			String currentWord = wordsToTest.get(currentIndex);
			Festival.ttsEnglish(1,"Now spell");
			Festival.ttsMaori(getSliderValue(), currentWord, false);
			wordNumLabel.setText("Spell word " + (currentIndex+1) + " out of 5");
			attempts = 1;
			hintLabel.setText(toUnderscores(currentWord));
			startTimer();
		} else{
			Main.sceneSwitcher.loadRewardScene(this.score, rewardMap);
		}

		focusTextfield();
	}

	// setter function allowing the tested words to be passed into the controller class
	public void setWords(List<String> wordList) throws IOException, InterruptedException {
		this.wordsToTest = wordList;
	}

	// function to update the users score
	public void updateScore() {
		currentScore.setText("SCORE = " + this.score);
	}

	public String toUnderscores(String word) {
		char[] characters = word.toCharArray();
		StringBuilder hiddenWord = new StringBuilder();
		for(char c : characters){
			if (Character.isLetter(c)) {
				hiddenWord.append("_ ");
			} else {
				hiddenWord.append(" ");
			}
		}	
		return hiddenWord.toString();
	}

	//function to change the colour of the multiplier rectangle based off the remaining time
	private void colourMultiplier() {
		FillTransition greenToYellow = new FillTransition(Duration.seconds(8), multiplierRectangle,Color.FORESTGREEN, Color.KHAKI);
		FillTransition yellowToOrange = new FillTransition(Duration.seconds(6), multiplierRectangle,Color.KHAKI, Color.DARKORANGE);
		FillTransition orangeToRed= new FillTransition(Duration.seconds(6), multiplierRectangle,Color.DARKORANGE, Color.TOMATO);
		greenToYellow.play();
		yellowToOrange.setDelay(Duration.seconds(9));
		yellowToOrange.play();
		orangeToRed.setDelay(Duration.seconds(14));
		orangeToRed.play();
	}

	protected void disableButtons(Boolean b) {
		speakButton.setDisable(b);
		dontKnowButton.setDisable(b);
	}
}