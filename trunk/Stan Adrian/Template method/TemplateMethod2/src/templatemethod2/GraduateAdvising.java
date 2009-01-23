package templatemethod2;

/**
 * 
 * 
 * GraduateAdvising extinde clasa Advising.
 * Un obiect creat folosind clasa GraduateAdvising poate fi folosit 
 * oriunde un obiect Advising este folosit.
 * 
 */

public class GraduateAdvising extends Advising {

    /**
     * 
     * Pentru acest pas al proecsului clasa GraduateAdvising
     * defineste propria implementare pentru metoda findAdvisor
     * din moment ce fiecare clasa copil al clasei Advising
     * trebuie sa faca ceva diferit in aceasta etapa a algoritmului.
     */
	public void findAdvisor() {
		System.out.println("Gasire consilier pentru student promovat...");
	}

	
    @Override
	public String toString() {
		
		return "Avizare proces pentru student promovat" ;
		
	}
	
}
