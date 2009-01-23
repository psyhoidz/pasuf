package abstractfactory2;

public class MainClass {

	public static void main(String[] args) {
		GreekSalataInstructiuniClient c = new GreekSalataInstructiuniClient(new TaiereGreekSalataInstructiuniFactory());
		c.printGreekSalataInstructiuni();
	}
	
}
