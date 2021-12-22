package controllers;

import java.io.IOException;
import java.util.List;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

// class providing functionality to the rewards screen in the app
public class SummaryController {
	
	@FXML
	Label correctLabel;
	@FXML
	Label accuracyLabel;
	@FXML
	Label attemptedLabel;
	private List<String> wordsToPlayAgain;
	
	/**
	 * This method updates the scenes various statistic labels using the parameters.
	 * Words to play again are set incase the user selects the option from the summary screen.
	 * 
	 * @param words
	 * @param correct
	 * @param attempted
	 */
	public void setSummaryDetails(List<String> wordsToPlayAgain, int correct, int attempted) {
		this.wordsToPlayAgain = wordsToPlayAgain;
		correctLabel.setText("Correct: " + correct);
		attemptedLabel.setText("Attempted: "+ attempted);
		double accuracy = Math.round(((double)correct/(double)attempted)*100*100.0)/100.0;
		accuracyLabel.setText("Accuracy: "+accuracy+" %");
	}
	
	////**** Various methods for navigating between scenes ****////
	
	// providing functionality to the play again button
	public void tryAgain(ActionEvent event) throws IOException, InterruptedException {
		Main.sceneSwitcher.loadPracticeScene(wordsToPlayAgain);
	}
	
	// providing functionality to the main menu button
	public void mainMenu(ActionEvent event) throws IOException {
		Main.sceneSwitcher.loadMenuScene();
	}
	
	public void newWords(ActionEvent event) throws IOException {
		Main.sceneSwitcher.loadTopicScene("practice");
	}
	
}
