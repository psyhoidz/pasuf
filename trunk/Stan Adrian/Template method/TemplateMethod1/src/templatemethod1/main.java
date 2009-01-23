/*
 * 
 *  Acest cod arata cum template method poate fi utilizat sa defineasca
 *  o implementare variabila pentru o operatie comuna. 
 *  In acest caz ProjectItem abstract class defineste metoda
 *  getCostEstimat care este o combinatie pentru costul timpului si materiale.
 *  Cele 2 clase concrete folosite in acest cod sunt Task si Deliverable, care
 *  au metode diferite pentru a calcula costul
 * 
 * 
 * 
 */

package templatemethod1;



public class main {
    public static void main(String [] arguments){

        
      
        Task primaryTask = new Task("Pentru a pun un JVM pe luna", "Misiune spatiala parte a programului JavaSpace", 240.0, 100.0);
        primaryTask.addProjectItem(new Task("Stabilirea controlului", "", 1000.0, 10.0));
        primaryTask.addProjectItem(new Task("Pregatirea oamenilor", "", 80.0, 30.0));
        
        Deliverable deliverableOne = new Deliverable("Aselenizare lunara", "", 2800, 40.0, 35.0);
        
        System.out.println("Cost total estimat: " + primaryTask);
        System.out.println("\t" + primaryTask.getCostEstimate());
        System.out.println();
        
        System.out.println("Cost total estimat: " + deliverableOne);
        System.out.println("\t" + deliverableOne.getCostEstimate());
    }
}