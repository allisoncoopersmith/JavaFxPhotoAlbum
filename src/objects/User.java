package objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class models a photo album user
 */

public class User implements Serializable{
	/**
	 * A user's username
	 */
	String username;
	/**
	 * All the user's albums
	 */
	ArrayList<Album> albums;
	
	/**
	 * This is the constructor for the User. It accepts a name and makes a new ArrayList of albums
	 * @param	username
	 */

	public User (String username) {
		this.username = username;
		this.albums= new ArrayList<Album>();
	}
	/**
	 * Returns the username
	 * @return	String
	 */
	public String getUsername() {
		return this.username; 
	}
	/**
	 * Returns the user's albums
	 * @return	albums
	 */
	public ArrayList<Album> getAlbums() {
		return this.albums;
	}
	/**
	 * Returns the username
	 * @param	name
	 * @return	true if the user is in the list
	 */
	public boolean checkIfAlbumExists (String name) {
		for (Album album: albums) {
			if (album.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * Adds an album to the list
	 * @param	album
	 */
	public void addAlbum(Album album) {
		albums.add(album);
	}
	/**
	 * Deletes an album from the list
	 * @param string
	 * @return	true if the user is deleted
	 */

	public boolean deleteAlbumByName (String string) {
		for (Album album: albums) {
			if (album.getName().equals(string)) {
				albums.remove(album);
				return true;
			}
		}
		return false;
	}
	/**
	 * Returns a string representation of a user
	 * @return	name
	 */
	public String toString() {
		return this.username;
	}

}


