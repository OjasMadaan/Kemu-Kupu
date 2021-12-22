package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

// class providing functionality to the rewards screen in the app
public class RewardController implements Initializable {

	@FXML
	Label messageLabel;
	@FXML
	Label englishLabel;
	@FXML
	Label scoreLabel;
	@FXML
	ImageView res1, res2, res3, res4, res5, leftTrophy, rightTrophy;
	@FXML
	Label word1, word2, word3, word4, word5;

	Image tick = new Image(getClass().getResourceAsStream("/images/tick.png"));
	Image cross = new Image(getClass().getResourceAsStream("/images/cross.png"));
	Image skip = new Image(getClass().getResourceAsStream("/images/skip.png"));
	Image gold = new Image(getClass().getResourceAsStream("/images/trophy1.png"));
	Image silver = new Image(getClass().getResourceAsStream("/images/trophy2.png"));
	Image bronze = new Image(getClass().getResourceAsStream("/images/trophy3.png"));

	List<ImageView> imageList;
	List<Label> wordList;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		imageList = new ArrayList<>(Arrays.asList(res1, res2, res3, res4, res5));
		wordList = new ArrayList<>(Arrays.asList(word1,word2,word3,word4,word5));
	}

	/**
    * Function sets custom supporting message and trophy depending on score range
    *
    *
    * @param score
    */
	public void setScore(double score) {
		scoreLabel.setText(String.valueOf(score));

		if ( score>=0 && score<10 ) {
			messageLabel.setText("Kia manawanui!");
			englishLabel.setText("Hang in there!");
			setTrophy(bronze);
			return;
		} else if (score>=10 && score<25) {
			messageLabel.setText("Okea ururoatia!");
			englishLabel.setText("Don't give up");
			setTrophy(silver);
			return;
		} else if (score>=25 && score<40) {
			messageLabel.setText("Karawhiua!");
			englishLabel.setText("Give it heaps! Give it all you got!");
			setTrophy(silver);
			return;
		} else if (score >= 40 && score<70) {
			messageLabel.setText("Kei reira!");
			englishLabel.setText("Right on! That's the one!");
			setTrophy(gold);
			return;
		} else if (score >=70 && score <=100) {
			messageLabel.setText("Ka mau te wehi!");
			englishLabel.setText("That's outstanding!");
			setTrophy(gold);
			return;
		}

	}

	/**
    * Function sets the words to be displayed in the reward screen
    * Also sets the correct/incorrect/skip images
    *
    * @param rewardMap
    */
	public void setWords(LinkedHashMap<String, String> rewardMap) {
		int i=0;
		for (String word : rewardMap.keySet()) {
			String status = rewardMap.get(word);

			wordList.get(i).setText(word);

			if (status.equals("correct")) {
				imageList.get(i).setImage(tick);
			} else if (status.equals("incorrect")) {
				imageList.get(i).setImage(cross);
			} else if (status.equals("skip")) {
				imageList.get(i).setImage(skip);
			}

			i++;
		}
	}

	// providing functionality to the play again button
	public void playAgain(ActionEvent event) throws IOException {
		Main.sceneSwitcher.loadTopicScene("quiz");
	}

	// providing functionality to the main menu button
	public void mainMenu(ActionEvent event) throws IOException {
		Main.sceneSwitcher.loadMenuScene();
	}

	private void setTrophy(Image colour) {
		leftTrophy.setImage(colour);
		rightTrophy.setImage(colour);
	}
}
