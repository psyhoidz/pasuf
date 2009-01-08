package iterator2;

/**
 * @author Tudor
 *
 */
public class SortedListIterator implements Iterator<String> {

	SortedList list;
	Node<String> currentNode;
	
	
	/**
	 * Constructor
	 */
	SortedListIterator(SortedList list){
		this.list = list;
		currentNode = null;
	}
	
	@Override
	public String CurrentItem() {
		if(currentNode != null) {
			return currentNode.data;
		}
		return null;
	}

	@Override
	public void First() {
		currentNode = list.first;
	}

	@Override
	public void Next() {
		if(currentNode != null)
			currentNode = currentNode.next;		
	}

	@Override
	public boolean isDone() {
		if(currentNode == null)
			return true;
		if(currentNode.next == null)
			return true;
		return false;
	}

}
