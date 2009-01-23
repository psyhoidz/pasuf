package abstractfactory2;


   /**
 * 
 * GreekSalataInstructiuniClient.java - (Client)
 * 
 * 

 * 
 * GreekSalataInstructiuniClient utilizeaza AbstractFactory si AbstractProduct 
 * pentru a determina instructiuni pentu creearea salatei. Depinzand de
 * ConcreteFactory salata poate fi feliata sau taiata.
 * 
 */


public class GreekSalataInstructiuniClient {
    
	
	private RosieInstructiuni RosieInstructiuni;
	private CastraveteInstructiuni CastraveteInstructiuni;

	
	public GreekSalataInstructiuniClient(SalataInstructiuniKit factory) {
		RosieInstructiuni = factory.createRosieInstructiuni();
		CastraveteInstructiuni = factory.createCastraveteInstructiuni();	
	}
	
	
	public void printGreekSalataInstructiuni() {
		RosieInstructiuni.printInstructiuni();
		CastraveteInstructiuni.printInstructiuni();
	}
	
	
} // class GreekSalataInstructiuniClient
