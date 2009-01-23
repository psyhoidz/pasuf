/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package abstractfactory3;

public class main {
  	private Computer comp;

/*clasa getComputer decide ce obiect trebuie sa creze*/
        
 public Computer getComputer(String computerType) {
  	  	if (computerType.equals("PC"))
comp = new PC();
else if(computerType.equals("Workstation"))
comp = new Workstation();
else if(computerType.equals("Server"))
comp = new Server();

return comp;
  	} 	 

        
        
public static void main(String[] args) {
  	  	main type = new main();

Computer computer = type.getComputer("Server");
System.out.println("Monitor: "+computer.getMonitor().getSpecification());
System.out.println("RAM: "+computer.getRAM().getSpecification());
System.out.println("Processor: "+computer.getProcessor().getSpecification());
  	}  	 
  	  	


}
