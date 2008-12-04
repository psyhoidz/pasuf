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
		
		System.out.println("The List:");
		ListIterator i = (ListIterator)l.CreateIterator();
		for(i.First(); i.IsDone(); i.Next()) {
			System.out.println("->"+(Integer)(i.CurrentItem()));
		}
	}

}
