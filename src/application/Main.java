package application;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	
	public static Stage primaryStage; // can be used by SceneController to create new scenes
	public static SceneManager sceneSwitcher;
	public static UserManager userManager;
	
    @Override
    public void start(Stage primaryStage) throws Exception{
    	// ensure files exist for user functionality
    	bashCmd("mkdir -m777 .users");
    	bashCmd("touch .users/.userList.txt");
    	// create static managers to be used throughout application.
    	sceneSwitcher = new SceneManager();
    	userManager = new UserManager();
    	
    	// load opening scene
        Main.primaryStage = primaryStage;
        sceneSwitcher.loadOpeningScene();
    }


    public static void main(String[] args) {
        launch(args);
    }
    
    
    /**
     * Runs the given command in bash terminal
     * 
     * @param command
     */
    public static void bashCmd(String command){
        try {
        	// run process with command
            ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
            Process process = processBuilder.start();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

