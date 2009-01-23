package templatemethod2;


/**
 * 
 *
 * Avceasta clasa abstracta trebuie sa fie extinsa de catre o clasa copil.
 * Dar toate clasele copil trebuie sa urmeze acelasi algotim 
 * pentru Advising. Totusi, clasa copil ca oferi proprile implementari
 * pentru uni dintre pasi
 * 
 */
public abstract class Advising {
	
	/**
	 * 
         * Template method serveste ca si sablon pentru un algoritm.
         * Nota: final insemna ca subclasele nu pot trece peste aceasta 
         * metoda si prin urmare trebuie sa urmeze acelasi algoritm in
         * assignAdvisorProcess().
         * Oricum , anumite metode apelate in algoritm vor fi executate 
         * de catre etode definite in clasele copil
         * Aceasta da claselor copil abilitatea sa schimbe modul in care 
         * anumiti pasi ai algoritmului sunt implementati
         * 
         * 
	 */
	public final void assignAdvisorProcess() {
		
		/*
                 * acestia sunt pasi care trebuie urmati de catre
                 * fiecare clasa copil
		*/
		getStudent();
		findAdvisor();
		assignAdvisor();
		
	}
	
	/**
         * Toate clasele copil vor mosteni aceasta metoda si va avea 
         * aceasi comporta pentru aceasta parte de algoritm.
	*/
	public void getStudent() {
		
		System.out.println("Gasire Student...");
		
	}
	
	/**
         * Din moment ce aceasta metoda este abstracta toate clasele 
         * copil vor trtebui sa defineasca un anumit timp de comportament 
         * pentru aceasta clasa. Aceasta da fiecarei clase copil
         * abilitatea sa specifice cum este implementata aceasta parte de 
         * algoritm
		 */
	public abstract void findAdvisor();
	
	
	/**
         * Toate clasele copil vor mosteni aceasta metoda si va avea 
         * aceasi comporta pentru aceasta parte de algoritm.
	*/
	public void assignAdvisor() {
		
		System.out.println("Alocare consilier...");
		
	}
	
	

}//end class
