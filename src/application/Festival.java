package application;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/** 
 * Class used for all festival text to speech bash calls
 **/
public class Festival {

	// define voices and speed parameters to be input into scm file.
	protected static String maori = "(voice_akl_mi_pk06_cg)";
	protected static String english = "(voice_cmu_us_awb_cg)";
	protected static String speed = "(Parameter.set 'Duration_Stretch 1.0)"; // default

	// This method sets the speed of voice synthesise in festival scm file format   
	protected static String speed(double speed) {
		Festival.speed = "(Parameter.set 'Duration_Stretch " + String.valueOf(speed) + ")";
		return Festival.speed;
	}

	// this method returns the appropriate sequence to speak a given word from a scm file.    
	protected static String word(String word) {
		return "(SayText " + '"' + word + '"' + ")";
	}

	// this method reads out a given maori word     
	public static void ttsMaori(double speed, String word, boolean isFirstWord) throws IOException, InterruptedException {
		// write a new scheme file with given parameters to be read out by festival bash command.
		writeSchemeFile(Arrays.asList(maori, speed(speed), word(word.toLowerCase())), ".maori.scm");

		ttsThread("festival -b .maori.scm", isFirstWord); 
	}

	// this method reads out a given English word/phrase
	public static void ttsEnglish(double speed, String word) throws IOException {
		// write new scheme file with given parameters
		writeSchemeFile(Arrays.asList(english, speed(speed), word(word)), ".english.scm");
		// process bash command    
		Main.bashCmd("festival -b .english.scm");
	}

	//Takes in a list of strings and writes a scm file, which can then be synthesised by festival
	private static void writeSchemeFile(List<String> list, String filename) throws IOException {
		FileWriter fw = new FileWriter(filename);

		for (String command : list) {
			fw.write(command + "\n");
		}

		fw.close();
	}

	/**
	 * Run the given command to synthesis text using festival.
	 * If it is the first word of a quiz/practice we run the command in a new thread,
	 * this allows the GUI to update before the speech synthesis begins. 
	 * 
	 * @param cmd
	 * @param firstWord
	 * @throws InterruptedException
	 */
	private static void ttsThread(String cmd, boolean firstWord) throws InterruptedException {
		if(firstWord) {
			// create new thread for festival call
			Thread taskThread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(250);
						Main.bashCmd(cmd);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});

			taskThread.start(); 
		}else {
			Main.bashCmd(cmd); // process command in bash terminal
		}
	}
}
