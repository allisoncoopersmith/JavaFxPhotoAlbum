/**
 * @author Allison Coopersmith
 * @author Kaushal Parikh
 */

package objects;

import java.io.Serializable;
/**
 * This class models a tag for a photo
 */
public class Tag implements Comparable<Tag>, Serializable{
	/**
	 * A string to hold the tag's key
	 */
	String key;
	/**
	 * A string to hold the tag's value
	 */
	String value;
	/**
	 * This is the constructor for the Tag. It accepts two String values.
	 * @param	key
	 * @param	value
	 */
	public Tag(String key, String value) {
		this.key = key;
		this.value=value;
	}
	/**
	 * Returns the tag's key
	 * @return	key
	 */
	public String getKey() {
		return this.key;
	}
	/**
	 * Sets the tag's key
	 * @param	key 	the string for the type of tag 
	 */
	public void setKey (String key) {
		this.key = key;
	}
	/**
	 * Returns the tag's value
	 * @return	value
	 */
	public String getValue() {
		return this.value;
	}
	/**
	 * Sets the tag's value
	 * @param	value 		string
	 */
	public void setValue(String value) {
		this.value=value;
	}
	/**
	 * Returns a string representation of the tag
	 */
	public String toString() {
		return  this.getKey() + "=" + this.getValue();
	}
	/**
	 * Orders the list of tags alphabetically
	 * @param	t 	tag to compare to
	 * @return	int value depending on which tag name appears first alphabetically
	 */
	@Override
	public int compareTo(Tag t) {
		if (getKey().toLowerCase().equals(t.getKey().toLowerCase())){
			return getValue().toLowerCase().compareTo(t.getValue().toLowerCase());
		} else {
			return getKey().toLowerCase().compareTo(t.getKey().toLowerCase());
		}
	}
	
}