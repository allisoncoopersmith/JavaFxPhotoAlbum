package objects;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class models a list of all users
 */
public class userList implements Serializable{

	public boolean stockBeenDeleted;
	/**
	 * An ID for serializing
	 */
	private static final long serialVersionUID = 3L;
	/**
	 * An ArrayList of users registered with the system
	 */
	public ArrayList<User> users;
	/**
	 * A string for the seralizable interface to store the directory of users
	 */
	public final static String storeDir = "dat";
	/**
	 * A string for the seralizable interface to store the directory of users
	 */
	public final static String storeFile = "users.dat";

	/**
	 * This is the constructor for the userList. It accepts no params and makes a new ArrayList of users
	 */
	public userList() {
		users = new ArrayList<User>();
	}

	/**
	 * Adds a user to the list
	 * @param	user
	 */
	public void addUser(User user) {
		users.add(user);
	}

	/**
	 * A string for the seralizable interface to store the directory of users
	 * @return	userList
	 */
	public ArrayList<User> getUsers() {
		return users;
	}
	/**
	 * Deletes a user from the list
	 * @param	user
	 */

	public void deleteUser(User user) {
		users.remove(user);
	}
	/**
	 * Checks if a user is in the list
	 * @param	username
	 * @return	user
	 */

	public User isUserInList(String username) {
		for (User u : users) {
			if (username.equals(u.getUsername())) {
				return u;
			}
		}

		return null;
	}
	/**
	 * Returns a string representation of the userList
	 * @return	names
	 */

	public String toString() {
		String userList = "";

		if (users == null) {
			return userList;
		}

		for (User u : users) {
			userList += u.toString() + "\n";
		}
		return userList;
	}
	public boolean hasStockBeenDeleted() {
		return stockBeenDeleted;
	}
	public void stockDeleted() {
		stockBeenDeleted=true;
	}
	/**
	 * Writes the userlist to a database
	 * @param	list
	 */

	public static void writeUsers(userList list) throws IOException{
		File directory = new File(storeDir);
		File f = new File(storeDir + File.separator + storeFile);

		if (!directory.exists()) {
			try {
				directory.mkdir();
			} catch (SecurityException se) {
				System.err.println("Could not make directory /dat.");
			}
		}

		if(f.exists() && !f.isDirectory()) { 
			try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile))){
				oos.writeObject(list);
				oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}

			try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile))){
				oos.writeObject(list);
				oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	/**
	 * Reads the users from a database
	 * @throws	IOException
	 * @throws	ClassNotFoundException
	 */

	public static userList readUsers() throws IOException, ClassNotFoundException {
		File directory = new File(storeDir);
		File f = new File(storeDir + File.separator + storeFile);

		if (!directory.exists()) {

			try {

				directory.mkdir();
			} catch (SecurityException se) {
				System.err.println("Could not make directory /dat.");
			}
		}


		if(f.exists() && !f.isDirectory()) { 

			try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storeDir + File.separator + storeFile))){

				userList list = (userList)ois.readObject();
				ois.close();

				return list;
			} catch (IOException e) {
			}
		} else {
			try {

				f.createNewFile();
			} catch (IOException e) {

			}
		}

		userList userList = new userList();
		return userList;


	}

}