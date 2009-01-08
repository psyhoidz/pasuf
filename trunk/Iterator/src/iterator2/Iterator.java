/**
 * Iterator interface
 */
package iterator2;

/**
 * @author Tudor
 *
 */
public interface Iterator<T> {

	public void First();
	public void Next();
	public boolean isDone();
	public T CurrentItem();
	
}
