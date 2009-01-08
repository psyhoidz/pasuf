package iterator2;

/**
 * @author Tudor
 *
 */
public interface AbstractStructure {

	public void Append(Object obj);
	public void Remove(int index);
	public long Count();
	public Iterator CreateIterator();
	
}
