package templatemethod2;

/**
 * 
 * 
 * UnderGraduateAdvising extinde clasa Advising.
 * Un obiect creat folosind clasa GraduateAdvising poate fi folosit 
 * oriunde un obiect Advising este folosit.
 * 
 */


public class UnderGraduateAdvising extends Advising {
	
      /**
     * 
     * Pentru acest pas al proecsului clasa UnderGraduateAdvising
     * defineste propria implementare pentru metoda findAdvisor
     * din moment ce fiecare clasa copil al clasei Advising
     * trebuie sa faca ceva diferit in aceasta etapa a algoritmului.
     */
    
	public  void findAdvisor() {
		
		System.out.println("Gasire consilier pentru student nepromovat...");
		
	}
	
    @Override
	public String toString() {
		
		return "Avizare proces pentru student nepromovat" ;
		
	}
	

}
