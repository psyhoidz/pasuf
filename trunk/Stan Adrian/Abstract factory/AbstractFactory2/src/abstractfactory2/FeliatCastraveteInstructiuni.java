package abstractfactory2;


/**
 * 
 * FeliatCastraveteInstructiuni.java - (ConcreteProduct)
 * 
 * The SlicedCucumberInstructions is created by a specific concrete
 * factory object and is the realization of a specific product type.
 * 
 * FeliatCastraveteInstructiuni este creat de un anumit concrete
 * factory object şi este realizare a unui specfic tip de produs.
 * 
 * 
 * 
 */
public class FeliatCastraveteInstructiuni extends CastraveteInstructiuni {

	public void printInstructiuni() {
		System.out.println("Feliat Castravete Instructiuni.");
		System.out.println("Pasul1 : Spala Castravetele bine in apa rece si lasal la uscat.");
		System.out.println("Pasul2 : Pune Castravetele vertical pe un suport de taiat.");
		System.out.println("Pasul3 : Foloseste un cutit pentru a taia Castravetele.");
	} // method printInstructiuni	
	
} // class FeliatCastraveteInstructiuni
