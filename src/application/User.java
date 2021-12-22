package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javafx.scene.image.Image;

public class User {
	
	private String name;
	private String icon;
	private String filename;
	private int attempted;
	private int correct;
	
	// Constructor for existing user 
	public User(String name) throws FileNotFoundException {
		this.setName(name);
		this.filename = "." + this.getName().replaceAll(" ", "_") + ".txt";
		importUserData();
	}
	
	// Constructor for a new user
	public User(String name, String icon) throws IOException {
		this.setName(name);
		this.filename = "." + this.getName().replaceAll(" ", "_") + ".txt";
		this.setAttempted(0);
		this.setCorrect(0);
		this.icon = icon;
		writeUserFile();
	}
	
	// read in user data from hidden file 
	private void importUserData() throws FileNotFoundException {
		Scanner filereader = new Scanner(new File(".users/" + this.filename));
		
		String line1 = filereader.nextLine();
		if (line1 != "") {
			this.setAttempted(Integer.parseInt(line1));
		}
		
		String line2 = filereader.nextLine();
		if (line2 != "") {
			this.setCorrect(Integer.parseInt(line2));
		}
		
		if (filereader.hasNextLine()) {
			this.icon = filereader.nextLine();
		}
		
		filereader.close();
	}
	
	// write/overwrite hidden file for user indicating statistics and the users icon image
	public void writeUserFile() throws IOException {
		FileWriter fw = new FileWriter(".users/" + this.filename);

		fw.write(String.valueOf(getAttempted()) + "\n");
		fw.write(String.valueOf(getCorrect()) + "\n");
		fw.write(icon + "\n");

		fw.close();
	}
	
	// update the users number of correct answers
	public void correct() throws IOException {
		this.correct++;
		writeUserFile();
	}
	
	// update the users number of attempts
	public void attempted() throws IOException{
		this.attempted++;
		writeUserFile();
	}
	
	
	////**** Various getters and setters ****////
	
	public String getFile() {
		return this.filename;
	}
	
	public double getAccuracy() {
		return Math.round(((double)getCorrect()/(double)getAttempted())*100*100.0)/100.0;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAttempted() {
		return attempted;
	}

	public void setAttempted(int attempted) {
		this.attempted = attempted;
	}

	public int getCorrect() {
		return correct;
	}

	public void setCorrect(int correct) {
		this.correct = correct;
	}
	
	public Image getIcon() {
		return new Image(getClass().getResourceAsStream("/images/" + this.icon + ".png"));
	}
	
}
