package iterator2;

/**
 * @author Tudor
 *
 */
public class SortedList implements AbstructStructure<String> {

	Node<String> first;
	long size;
	
	
	/**
	 * constructor
	 */
	SortedList() {
		first = null;
		size = 0;
	}
	
	@Override
	public void Append(String obj) {
		Node<String> node = new Node<String>(obj);
		if(first == null) { 
			first = node;
		} else if(first.data.compareTo(obj) > 0) {
			node.next = first;
			first = node;
		} else {
			Node<String> q1, q2;
			q1 = first;
			q2 = q1.next;
			while(q2 != null) {
				if(q2.data.compareTo(obj) >= 0)
					break;
				q1 = q2;
				q2 = q1.next;
			}
			q1.next = node;
			node.next = q2;
		}
		
	}

	@Override
	public long Count() {
		return size;
	}

	@Override
	public Object CreateIterator() {
		return new SortedListIterator(this);
	}

	@Override
	public void Remove(String obj) {
		// TODO Auto-generated method stub
		
	}

}
