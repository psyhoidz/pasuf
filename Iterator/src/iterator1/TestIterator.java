package iterator1;

public class TestIterator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		List l = new List();
		l.Append(new Integer(2));
		l.Append(new Integer(5));
		l.Append(new Integer(10));
		l.Append(new String("ceva"));
		
		System.out.println("The List:");
		Iterator i = l.CreateIterator();
		for(i.First(); i.IsDone(); i.Next()) {
			System.out.println("--" + i.CurrentItem());
		}
	}

}
