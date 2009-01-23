package abstractfactory2;


/**
 * 
 * FeliatGreekSalataInstructionFactory.java - (ConcreteFactory)
 * 
 *
 * 

 * FeliatGreekSalataInstructionFactory implementeaza lista de operatii
 * si creaza obiectele produsului concret care sunt asociate cu clasa 
 * concretefactory. Clasa concretefactory creeaza produse concrete specifice
 * clasei pentru a creea FeliereGreekSalata.
 * 
 * */

public class FeliatGreekSalataInstructionFactory implements SalataInstructiuniKit {

	public RosieInstructiuni createRosieInstructiuni() {
		return new FeliatRosieInstructiuni();
	}

	public CastraveteInstructiuni createCastraveteInstructiuni() {
		return new FeliatCastraveteInstructiuni();
	}

} // class FeliatGreekSalataInstructionFactory
