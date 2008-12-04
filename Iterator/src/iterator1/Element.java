/**
 * 
 */
package iterator1;

/**
 * @author Tudor
 *
 */
public class Element {

	// the data of the element
	public Object data;
	// a link to the next element
	public Element next;
	
	Element(Object value) {
		data = value;
		next = null;
	}
	
}
