/**
 * @author Allison Coopersmith
 * @author Kaushal Parikh
 */

package objects;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
/**
 * This class models a photo's image file
 */

public class photoImage implements Serializable, Comparable<photoImage> {

	/**
	 * This variable stores the actual image in a file variable.
	 */
	File imageFile;
	/**
	 * This variable stores the last modified date of the image.
	 */
	Date date;
	/**
	 * This is the constructor for the photoImage. It accepts a File and sets the date
	 * @param	imageFile
	 */
	public photoImage (File imageFile) {
		this.imageFile=imageFile;
		this.date = new Date(imageFile.lastModified());
	}
	/**
	 * Returns the photo's image file
	 * @return	file
	 */
	public File getImageFile() {
		return this.imageFile;
	}
	/**
	 * Returns the photo's date object
	 * @return	date
	 */
	public Date getDate() {
		return this.date;
	}
	/**
	 * Orders the photos by date
	 * @param	i	the photoimage that is passed to the method to be compared to
	 * @return	integer 	value depending on which photo was taken more recently
	 */
	public int compareTo(photoImage i) {
	    return getDate().compareTo(i.getDate());
	  }
	
}







