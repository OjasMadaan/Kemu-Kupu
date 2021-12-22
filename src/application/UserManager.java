package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class UserManager {
	private ArrayList<User> users;
	public User currentUser = null; // default to indicate Guest Mode
	private HashMap<String, User> userMap;
	
	// constructor for user manager object
	public UserManager() throws FileNotFoundException {
		setUserMap(new HashMap<>());
		setUsers(new ArrayList<User>());
		importUsers();
	}
	
	
	/**
	 * Read in list of users from hidden file.
	 * We use the usernames in the file to create existing users and
	 * add them to the relevant lists/maps
	 * 
	 * @throws FileNotFoundException
	 */
	private void importUsers() throws FileNotFoundException {
		// create new scanner for hidden file with list of users
		Scanner scanner = new Scanner(new File(".users/.userList.txt"));
		
		// scan through entire file
		while(scanner.hasNextLine()) {
			String username = scanner.nextLine().trim();
			if (!username.isBlank()) {
				User currentImport = new User(username); // create existing user
				getUserMap().put(username, currentImport);
				getUsers().add(currentImport);
			}
		}
	
		scanner.close();
	}
	

	/**
	 * Delete the user from the relevant data files and lists/maps.
	 * We then set the current user back to its default (guest mode)
	 * @param username
	 * @throws IOException
	 */
	public void removeUser(String username) throws IOException {
		User userToRemove = getUserMap().get(username);
		// remove from map
		getUserMap().remove(username);
		getUsers().remove(userToRemove);
		
		// delete user file
		Main.bashCmd("rm -f .users/" + userToRemove.getFile());
		// update user list
		updateUsersFile();
		
		this.currentUser = null;
	}
	
	
	/**
	 * Create a new user and update the relevant data files.
	 * We also set the user managers current user to the most recently created user
	 * 
	 * @param username
	 * @param icon
	 * @throws IOException
	 */
	public void addUser(String username, String icon) throws IOException {
		User newUser = new User(username, icon);
		// add to map
		getUserMap().put(username, newUser);
		getUsers().add(newUser);
		// update user list
		updateUsersFile();
		
		this.currentUser = newUser; 
	}
	
	// overwrite file which stores list of users.
	private void updateUsersFile() throws IOException {
		FileWriter fw = new FileWriter(".users/.userList.txt");
		for (String user : getUserMap().keySet()) {
			fw.write(user + "\n");
		}
		fw.close();
	}
	
	
	////**** Various getters and setters ****////
	
	public ArrayList<User> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}

	public HashMap<String, User> getUserMap() {
		return userMap;
	}

	public void setUserMap(HashMap<String, User> userMap) {
		this.userMap = userMap;
	}
	
	
	
}
