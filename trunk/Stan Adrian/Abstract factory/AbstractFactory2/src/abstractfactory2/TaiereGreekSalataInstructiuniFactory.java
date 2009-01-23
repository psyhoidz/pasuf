package abstractfactory2;


/**
 * 
 * TaiereGreekSalataInstructionFactory.java - (ConcreteFactory)
 * 
 *
 * 

 * TaiereGreekSalataInstructionFactory implementeaza lista de operatii
 * si creaza obiectele produsului concret care sunt asociate cu clasa 
 * concretefactory. Clasa concretefactory creeaza produse concrete specifice
 * clasei pentru a creea FeliereGreekSalata.
 * 
 * */

public class TaiereGreekSalataInstructiuniFactory implements SalataInstructiuniKit {

	public RosieInstructiuni createRosieInstructiuni() {
		return new TaiereRosieInstructiuni();
	}

	public CastraveteInstructiuni createCastraveteInstructiuni() {
		return new TaiereCastraveteInstructiuni();
	}
	
} // class TaiereGreekSalataInstructiuniFactory
