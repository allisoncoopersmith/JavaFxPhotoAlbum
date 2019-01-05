/**
 * @author Allison Coopersmith
 * @author Kaushal Parikh
 */

package objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class models a photo album.
 */
public class Album implements Serializable {
	
	/**
	 * This variable holds the album's name
	 */
	String name;
	
	/**
	 * This ArrayList holds the album's photos
	 */
	ArrayList<Photo> photos;
	
	/**
	 * This is the constructor for the Album. It accepts a name and makes a new ArrayList of photos.
	 * @param	name		this is the name of the album that is being created
	 */
	public Album (String name) {
		this.name = name;
		photos = new ArrayList<Photo>();
	}
	
	/**
	 * Adds a photo to the album
	 * @param	p	takes in the photo to be added
	 */

	public void addPhoto(Photo p) {
		photos.add(p);
	}
	/**
	 * Removes a photo from the album
	 * @param	p	takes in the photo that is searched for and then removed
	 */

	public void removePhoto (Photo p) {
		photos.remove(p);
	}
	/**
	 * Removes a photo from the album based on its image file
	 * @param	p	this removes a photo based on the photoImage instance within (the actual image that is displayed)
	 */

	public void removePhotoByImage(Photo p) {
		for (Photo photo: photos) {
			if (photo.getImage().equals(p.getImage())){
				photos.remove(photo);
				return;
			}
		}
	}
	
	/**
	 * Sets the album's name
	 * @param	name	this is the new name, what the album is updated to
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * Returns the album's name
	 * @return	String
	 */
	public String getName() {
		return this.name;
	}
	/**
	 * Returns the album's list of photos
	 * @return	photoArraylist
	 */
	public ArrayList<Photo> getPhotos() {
		return this.photos;
	}
	/**
	 * Returns a string representation of the album
	 * @return	String
	 */
	public String toString() {
		return this.name;
	}

}
