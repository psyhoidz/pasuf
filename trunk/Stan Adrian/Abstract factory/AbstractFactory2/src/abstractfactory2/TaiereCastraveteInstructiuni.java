package abstractfactory2;


/**
 * TaiereCastraveteInstructiuni.java - (ConcreteProduct)
 * 
 * TheTaiereCucumberInstructions is created by a specific concrete
 * factory object and is the realization of a specific product type.
 * 
 * TaiereCastraveteInstructiuni este creat de un anumit concrete
 * factory object şi este realizare a unui specfic tip de produs.
 */
public class TaiereCastraveteInstructiuni extends CastraveteInstructiuni {
	
	public void printInstructiuni() {
		System.out.println("Taiere Castravete Instructiuni.");
		System.out.println("Pasull : Taie coaja si apoi taie Castravetele in jumatate.");
		System.out.println("Pasul2 : Folosind o lingura, scoate semintele din interiorul castravetelui. faceti aceasta operatie pentru fiecare jumatate.");
		System.out.println("Pasul3 : Taie castravetele in feli mici si apoi taie invers pentru a obtine cubulete mici.");
	} // method printInstructiuni	
	
} // class TaiereCastraveteInstructiuni
