package iterator1;

/**
 * @author Tudor
 *
 */
public class ListIterator implements Iterator {

	List list;
	Element current;
	
	ListIterator(List _list) {
		list = _list;
		current = null;
	}
	
	@Override
	public Object CurrentItem() {
		// TODO Auto-generated method stub

		return current.data;
	}

	@Override
	public void First() {
		current = list.first;
	}

	@Override
	public boolean IsDone() {
		if(current == null)
			return false;
		return true;
	}

	@Override
	public void Next() {
		if(current != null)
			current = current.next;
	}

}
