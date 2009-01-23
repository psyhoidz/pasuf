package templatemethod2;

import java.util.ArrayList;


public class main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		ArrayList<Advising> advisingList = new ArrayList<Advising>();
		
		
		/*
                 * 
                 * Din moment ce ambele clase UnderGraduateAdvising si
                 * GraduateAdvising extind clsa Advising putem folosi
                 * obiecte create folosind aceste clase intr-un ArrayList
                 * specificat sa contina numai obiecte Advising
		 */
		advisingList.add( new UnderGraduateAdvising() );
		
		advisingList.add( new GraduateAdvising() );
		
		/*
                 * Nota: Astfel va fi mai usot in viitor sa se adauge
                 * alte tipuri de obyecte de tip Advising.
		 */
		
		for ( Advising advising : advisingList ) {
			
			System.out.println( advising ) ;
			
			/*
			 * 
                         * In acest moment ii spunem obiectului Advising sa execute
                         * algoritmul. din cauza polimorfismului  obiectul 
                         * specificat va executa propria sa metoda assignAdvisor
                         * Acest lucru ne permite de a utiliza o clasa şi o metoda
                         * dar sa obtinem comportari diferite cand este nevoie.
			 */
			advising.assignAdvisorProcess();
			
			System.out.println("\n");
			
			
		}//end for

	}//end main method

}//end class
