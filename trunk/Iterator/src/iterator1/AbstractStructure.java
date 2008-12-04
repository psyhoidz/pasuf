package iterator1;

/**
 * @author Tudor
 *
 */
public interface AbstractStructure {
	
	public void   Append(Object obj);
	public void   Remove(Object obj);
	public int    Count();
	public Object CreateIterator();
	
}
