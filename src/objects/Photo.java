/**
 * @author Allison Coopersmith
 * @author Kaushal Parikh
 */

package objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
/**
 * This class models a photo
 */
public class Photo implements Serializable{
	/**
	 * An ArrayList of the photo's tags
	 */
	ArrayList<Tag> tags;
	/**
	 * The photo's caption
	 */
	String caption;
	/**
	 * The photo's image
	 */
	photoImage image;
	
	/**
	 * This is the constructor for the Photo. It an image and a caption.
	 * @param	i 	the image associated with the photo
	 * @param	caption 	the caption associated with the photo
	 */

	public Photo (photoImage i, String caption) {
		this.image=i;
		this.caption=caption;
		tags = new ArrayList<Tag>();
	}
	/**
	 * Sets the photo's caption
	 * @param	caption 	the caption associated with the photo 
	 */
	public void setCaption (String caption) {
		this.caption=caption;
	}
	/**
	 * Returns the caption
	 * @return	caption 	the caption associated with the photo 
	 */
	public String getCaption() {
		return this.caption;
	}
	/**
	 * Adds a tag
	 * @param	t 	the tag associated with the photo to be added 
	 */
	public void addTag(Tag t) {
		tags.add(t);
	}
	/**
	 * Removes a tag
	 * @param	t 	the tag to be removed
	 */
	public void removeTag(Tag t) {
		tags.remove(t);
	}
	/**
	 * Returns a list of tags
	 * @return	tags 	associated with the photo 
	 */
	public ArrayList<Tag> getTags() {
		return this.tags;
	}
	/**
	 * Gets the photo's image
	 * @return	image 	associated with the photo 
	 */
	public photoImage getImage() {
		return this.image;
	}
	/**
	 * Returns a string representation of a photo
	 * @return	date photo was taken
	 */
	public String toString() {
		return this.getImage().getDate().toString();
	}
	
}