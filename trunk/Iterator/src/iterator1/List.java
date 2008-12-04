package iterator1;

/**
 * @author Tudor
 *
 */
public class List implements AbstractStructure {

	Element first, last;
	int size;
	
	List() {
		first = last = null;
		size = 0;
	}
	
	@Override
	public void Append(Object obj) {
		Element _new = new Element(obj);
		if(first == null) // this is the first nod
			first = _new;
		if(last != null)
			last.next = _new;
		last = _new;
		size++;
	}

	@Override
	public int Count() {
		return size;

	}

	@Override
	public Object CreateIterator() {
		// TODO Auto-generated method stub
		return new ListIterator(this);
	}

	@Override
	public void Remove(Object obj) {
		// search for the object that must be remove
		Element q1 = first, q2 = first;
		while(q2 != null) {
			if(q2.data.equals(obj)) { // we find it
				if(q2 != first)
					q1.next = q2.next;
				else
					first = first.next;
				return;
			}
			q1 = q2;
			q2 = q1.next;
		}
	}

}
