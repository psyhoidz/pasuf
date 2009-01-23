package abstractfactory2;


/**
 * SalataInstructiuniKit.java - (AbstractFactory)
 * 
 * 
 * 
 * SalataInstructiuniKit defineste un tip factory care listeaza operatile
 * pentru a creea un set de tipuri de produse legate de abstract. Fiecare
 * clasa concrete factory ( DicedGreekSaladInstructionFactory.java si
 *  SlicedGreekSaladInstructionFactory.java ) va creea o clasa product 
 * specifica.
 * 
 */


public interface SalataInstructiuniKit {

	
	public RosieInstructiuni createRosieInstructiuni();
	
	
	public CastraveteInstructiuni createCastraveteInstructiuni();
	
		
} // class SalataInstructiuniKit
