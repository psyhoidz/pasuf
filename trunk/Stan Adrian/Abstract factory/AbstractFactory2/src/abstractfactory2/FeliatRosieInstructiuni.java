package abstractfactory2;


/**
 * 
 * FeliatRosieInstructiuni.java - (ConcreteProduct)
 * 
  * 
 * FeliatRosieInstructiuni este creata de catre un concrete 
 * factory object si este realizarea unui tip specific de produs
 * 
 * 
 */
public class FeliatRosieInstructiuni extends RosieInstructiuni {
	
	public void printInstructiuni() {
		System.out.println("Feliat Rosie Instructiuni.");
		System.out.println("Pasul1 : Spala Rosia bine in apa rece si lasal la uscat");
		System.out.println("Pasul2 : Foloseste un cutit special pentru a taia capatul de la varful rosiei.");
		System.out.println("Pasul3 : Foloseste un cutit pentru a taia Rosia.");
	} // method printInstructiuni	
	
} // class FeliatRosieInstructiuni
