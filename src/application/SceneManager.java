package application;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import controllers.PracticeController;
import controllers.QuizController;
import controllers.RewardController;
import controllers.SummaryController;
import controllers.TopicController;
import controllers.UserController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

// This manager class is used by all other controllers to load scenes.
public class SceneManager {

	Stage stage;
	Scene scene;

	@FXML
	Button startButton, practiceButton, userProfileButton;
	@FXML
	ImageView userProfileImage;

	/**
	 * This method loads up the initial screen of the application
	 * It also does a pre-check if there are any existing users,
	 * if not, then certain elements are disabled.
	 * 
	 * @throws IOException
	 * 
	 */
	public void loadOpeningScene() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/OpeningScene.fxml"));
		Parent root = loader.load();
		// Create instance of controller
		UserController userController = loader.getController();

		// check if there are any existing users in game.
		if (Main.userManager.getUsers().isEmpty()) {
			userController.returningUserButton.setDisable(true);
		}

		displayScene(root);	
	}


	/**
	 * This method is used to load up the home screen (main menu).
	 * It also sets the icon image for the user profile button if a user has been selected.
	 * 
	 * @throws IOException
	 */
	public void loadMenuScene() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/Main.fxml"));
		Parent root = loader.load();
		SceneManager controller = loader.getController();

		// if current user is not 'guest' (null)
		if (Main.userManager.currentUser != null) {
			controller.userProfileImage.setImage(Main.userManager.currentUser.getIcon());
		} else {
			// disable user profile if current user is guest
			controller.userProfileButton.setDisable(true);
		}

		displayScene(root);
	}

	/**
	 * This method loads up the topic selection screen.
	 * It checks if the button which loaded it was the "quiz" or "practice" button.
	 * It sets the topic controllers mode so that the word lists can be created accordingly.
	 * 
	 * @param event (button press)
	 * @throws Exception
	 * 
	 */
	@FXML
	public void loadTopicScene(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/Topics.fxml"));
		// load topic FXML
		Parent root = loader.load();
		// Create instance of controller
		TopicController topicController = loader.getController();

		String fxid = ((Button) event.getSource()).getId();
		if (fxid.equals("startButton")) {
			topicController.setMode("quiz");
		} else {
			topicController.setMode("practice");
		}

		displayScene(root);
	}


	/**
	 * This method is invoked if the user chooses the option to play again (with new words)
	 * It loads up the topic selection scene, and sets the mode to whichever type of game mode 
	 * it was invoked from.
	 * 
	 * @param mode
	 * @throws IOException
	 * 
	 */
	public void loadTopicScene(String mode) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/Topics.fxml"));
		Parent root = loader.load(); // load topic FXML

		// Create instance of controller
		TopicController topicController = loader.getController();
		topicController.setMode(mode);

		displayScene(root);
	}



	/**
	 * This method loads up the quiz scene.
	 * It creates an instance of the quiz controller. This is used to
	 * set the words list, update the hint label, and speak the first word.
	 * 
	 * @param wordsList
	 * @throws IOException
	 * @throws InterruptedException
	 * 
	 */
	public void loadQuizScene(List<String> wordsList) throws IOException, InterruptedException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/Quiz.fxml"));
		Parent root = loader.load();
		// Create instance of controller
		QuizController quizController = loader.getController();

		// set words for quiz
		quizController.setWords(wordsList);
		// update hint label showing number of letters.
		String firstWord = wordsList.get(0);
		quizController.hintLabel.setText(quizController.toUnderscores(firstWord));

		// Call festival to read out first word of quiz without waiting for GUI. 
		Festival.ttsMaori(1,firstWord, true);

		displayScene(root);
	}

	/**
	 * This method loads up the practice scene.
	 * It sets the word list for the practice module, updates the hint label and 
	 * speaks the first word.
	 * 
	 * @param wordsList
	 * @throws IOException
	 * @throws InterruptedException
	 * 
	 */
	public void loadPracticeScene(List<String> wordsList) throws IOException, InterruptedException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/Practice.fxml"));
		Parent root = loader.load();
		// Create instance of controller
		PracticeController practiceController = loader.getController();

		// set words for practice
		practiceController.setWords(wordsList);
		// update hint label showing number of letters.
		String firstWord = wordsList.get(0);
		practiceController.hintLabel.setText(practiceController.toUnderscores(firstWord));

		// Call festival to read out first word to practice without waiting for GUI.
		Festival.ttsMaori(1,wordsList.get(0), true);

		displayScene(root);
	}


	/**
	 * Loads the post-quiz reward screen and updates the words quizzed, their status and the users score.
	 * 
	 * @param score from quiz round
	 * @param rewardMap where each key is a word and each value is the result of the attempt.
	 * @throws IOException
	 * 
	 */
	public void loadRewardScene(double score, LinkedHashMap<String, String> rewardMap) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/Reward.fxml"));
		Parent root = loader.load();
		// Create instance of controller
		RewardController controller = loader.getController();

		controller.setWords(rewardMap); // pass in quizzed words and their status
		controller.setScore(score); // Set score (this will update the scene with appropriate labels)

		displayScene(root);
	}


	/**
	 * This method loads the summary screen after a practice session
	 * It sets the statistics in the summary screen using the parameters.
	 * 
	 * @param words
	 * @param correct
	 * @param attempted
	 * @throws IOException
	 */
	public void loadSummaryScene(List<String> wordsToPlayAgain, int correct, int attempted) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/Summary.fxml"));
		Parent root = loader.load();
		// Create instance of controller
		SummaryController controller = loader.getController();
		controller.setSummaryDetails(wordsToPlayAgain, correct, attempted);

		displayScene(root);
	}


	/**
	 * Loads the scene where player can create a new user
	 * 
	 * @throws IOException
	 */
	public void loadCreateUserScene() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/CreateUser.fxml"));
		Parent root = loader.load();
		displayScene(root);
	}

	
	/**
	 * Loads scene where player can view existing users
	 * 
	 * @throws IOException
	 */
	public void loadReturningUserScene() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/ReturningUser.fxml"));
		Parent root = loader.load();
		// Create instance of controller
		UserController controller = loader.getController();
		controller.showUser(); // update scene with first users details

		displayScene(root);
	}

	/**
	 * Loads user profile scene where player can view their all-time statistics
	 * 
	 * @param event (button)
	 * @throws IOException
	 */
	@FXML
	public void loadUserProfileScene(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/UserProfileScene.fxml"));
		Parent root = loader.load();
		// Create instance of controller
		UserController controller = loader.getController();
		controller.showProfileView();
		
		displayScene(root);
	}

	
	/**
	 * Logs user out (sets current user to null) then returns
	 * player to opening screen of app.
	 * 
	 * @param event
	 * @throws IOException
	 */
	public void logOut(ActionEvent event) throws IOException {
		Main.userManager.currentUser = null;
		loadOpeningScene();
	}

	/**
	 * Displays the window with a root from an fxml file.
	 * 
	 * @param root
	 */
	private void displayScene(Parent root) {
		stage = Main.primaryStage;
		scene = new Scene(root);
		stage.setTitle("Kemu Kupu - Spelling"); // set title for gui window
		stage.setResizable(false);
		stage.setScene(scene);
		stage.show();
	}

}
