package controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

/**
 * 
 * This class provides functionality to the topic selection screen
 *
 */
public class TopicController implements Initializable {

	private ObservableList<String> observableTopicList = FXCollections.observableArrayList();
	protected HashMap<String,String> topicMap = new HashMap<>();
	private String pathToWords = "wordLists/";
	private String mode;

	@FXML
	protected ComboBox<String> topicBox;
	@FXML 
	private Label topicLabel;

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// following code is run when the topic page is loaded
		importTopics();
		populateTopicBox();
	}

	// Function adds the topics to the dropdown menu
	@FXML
	public void populateTopicBox() {
		topicBox.getItems().add("Random (Default)");
		topicBox.getItems().addAll(observableTopicList.sorted());
		topicBox.getSelectionModel().selectFirst();
	}

	// Return to main menu
	@FXML
	public void mainMenu(ActionEvent event) throws IOException {
		Main.sceneSwitcher.loadMenuScene();
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

    // Function gets the names of the topics based off of the file name.
	public void importTopics(){
		File wordFolder = new File(this.pathToWords);

		for (File file : wordFolder.listFiles()) {
			// format topic name from file name
			String topic = file.getName().replace(".txt","").replace("_", " ");
			topicMap.put(topic, file.getName());
			observableTopicList.add(topic);
		}

		// Select a random word file for Default option
		Collections.shuffle(observableTopicList);
		String randomTopic = observableTopicList.get(0);
		topicMap.put("Random (Default)", topicMap.get(randomTopic));

	}

	/**
     * Given a topic, function returns a list of words from the list
     *
     *
     * @param String topic
     * @return String list
     */

	public List<String> getWordsFromFile(String topic) throws IOException {
		// Function to get random words from a given word list
		List<String> listOfWords = new ArrayList<>();
		
		Scanner scanner = new Scanner(new File(pathToWords + topicMap.get(topic)));
		while (scanner.hasNextLine()) {
			listOfWords.add(scanner.nextLine().trim());
		}
		scanner.close();
		
		Collections.shuffle(listOfWords);
		
		if (mode.equals("quiz")) {
			return listOfWords.subList(0, 5);
		} else {
			return listOfWords; // return entire list for practice mode.
		}

	}
	/**
     * Method gets topic from drop down list and loads up the game with
     * the topic.
     *
     * @throws Exception
     * @return String list
     */

	@FXML
	public void loadGame(ActionEvent e) throws Exception {
		// load new quiz scene
		if (!topicBox.getSelectionModel().isEmpty()) { // makes sure a topic is selected
			String topic = topicBox.getSelectionModel().getSelectedItem();
			List<String> wordsList = getWordsFromFile(topic);

			if (mode.equals("practice")) {
				Main.sceneSwitcher.loadPracticeScene(wordsList);
			}
			if (mode.equals("quiz")) {
				Main.sceneSwitcher.loadQuizScene(wordsList);
			}
		}
	}


}
