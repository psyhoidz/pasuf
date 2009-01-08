package iterator2;

public class TestIterator {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		FixedArray agregate = new FixedArray(12);
		Iterator i = agregate.CreateIterator();
		
		try{
			agregate.Append(new Integer(2));
			agregate.Append(new Integer(5));
			agregate.Append(new Integer(10));
			agregate.Append(new String("ceva"));
			agregate.Append(new String("altceva"));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("The List:");
		for(i.First(); i.isDone(); i.Next()) {
			System.out.println("--" + i.CurrentItem());
		}
		
		agregate.Remove(1);
		
		System.out.println("The List:");
		for(i.First(); i.isDone(); i.Next()) {
			System.out.println("--" + i.CurrentItem());
		}
	}

}
