package iterator2;

/**
 * @author Tudor
 *
 */
public class FixedArrayIterator implements Iterator {

	FixedArray array;
	int i = -1;
	
	FixedArrayIterator(FixedArray a){
		this.array = a;
	}
	
	@Override
	public Object CurrentItem() {
		if(i!=-1 && i!=array.size)
			return array.data[i];
		return null;
	}

	@Override
	public void First() {
		if(array.nrItems > 0)
			i = 0;
	}

	@Override
	public void Next() {
		if(i < array.nrItems)
			i++;
	}

	@Override
	public boolean isDone() {
		if(i == array.nrItems)
			return false;
		return true;
	}

}
