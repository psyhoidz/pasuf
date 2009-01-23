package abstractfactory2;


/**
 * 
 * TaiereRosieInstructiuni.java - (ConcreteProduct)
 * 
  * 
 * TaiereRosieInstructiuni este creata de catre un concrete 
 * factory object si este realizarea unui tip specific de produs
 * 
 * 
 */

public class TaiereRosieInstructiuni extends RosieInstructiuni {

	public void printInstructiuni() {
		System.out.println("Taiere Rosie Instructiuni.");
		System.out.println("Pasul1 : Taie Rosia în sferturi pe verticală, prin stem.");
		System.out.println("Pasul2 : Taie apoi miezul din fiecare sfert.");
		System.out.println("Pasul3 : Taie apoi restul in fasii subtiri iar apoi taiati invers pentru a obtine cubulete mici.");
	} // method printInstructiuni
	
} // class TaiereRosieInstructiuni
