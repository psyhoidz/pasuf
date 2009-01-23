/*Sa presupunem ca avem nevoie sa obtinem specificatile mai multor 
 component ale unor calculatoare care sunt de mai multe tipuri. 
 Diferntele componente ale calculatoruluii ar fi : monitorul, RAM si 
 processor.  Diferitele tipuri de calculatoare sunt PC, Workstation si 
 Server.
Deci , avem nevoie de o clasa Abstracta care sa se numeasca Computer.
*/

package abstractfactory3;

public abstract class Computer {
  	/**
*Metoda abstrcta returneaza partile ideale ale unui calculatorului  
* de tip Server PC sau Workstation
* @return Parts
*/
public abstract Parts getRAM();

public abstract Parts getProcessor();

public abstract Parts getMonitor();
}

/*Aceasta  clasa, dupa cum ati vazut, are trei metode toate 
 returnand diferitele tipuri ale calculatorului. Toate returneaza 
 o metoda numita Parts. Specificatiile pentru Partsvor fi diferite 
 pentru fiecare dintre tipurile de calculatoare. Sa privim un pic 
 la clasa Parts.*/