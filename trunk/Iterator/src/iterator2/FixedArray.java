package iterator2;

public class FixedArray implements AbstractStructure {

	Object[] data;
	int size, nrItems;
	
	/**
	 * Constructor
	 * 
	 * @param size the size of the array
	 */
	FixedArray(int size) {
		data = new Object[size];
		this.size = size;
		nrItems = 0;
	}
	
	@Override
	public void Append(Object obj) {
		if(nrItems < size)
			data[nrItems++] = obj;
		
	}

	@Override
	public long Count() {
		return nrItems;
	}

	@Override
	public Iterator CreateIterator() {
		return new FixedArrayIterator(this);
	}

	@Override
	public void Remove(int index) {
		if(index >= nrItems)
			return;
		else if(index == (nrItems-1))
			nrItems--;
		else {
			for(int i=index+1; i<nrItems; i++)
				data[i-1] = data[i];
			nrItems--;
		}
			
	}

}
