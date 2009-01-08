package iterator2;

/**
 * @author Tudor
 *
 */
public interface AbstructStructure<E> {

	public void Append(E obj);
	public void Remove(E obj);
	public long Count();
	public Object CreateIterator();
	
}
