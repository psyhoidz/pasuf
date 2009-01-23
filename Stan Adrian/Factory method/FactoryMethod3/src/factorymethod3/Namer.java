/*
 Sa consideram un simplu exemplu undeputem sa folosim o clasa Factory. 
 Sa presupunem ca avem o intrare si vrem sa lasam useru sa introduca 
 numele sau fie sub forma “Prenume Nume” fie sub forma “Nume, Prenume”.  
 Pentru a simplifica problema folosim o regula pentru a decidea ordinea 
 datelor prin folosire virgulei intre nume si prenume. Aceasta este o
 simpla problema care s-ar putea face intr-o singura clasa dar vrem sa 
 ilustram care este principiul sablonului factory method. Incepem prin a
 define o simpla clsa care primeste un nume si il imparte in 2 nume:
 */

package factorymethod3;

class Namer {
//o clasa simpla care imparta numele in 2
protected String last;//variabila unde o sa tinem numele
protected String first; //variabila unde o sa tinem prenumele
public String getFirst() {
return first; //return prenumele
}
public String getLast() {
return last; //return numele
}
}

/*In aceasta clasa practic nu am testat nimic, dar am folosit 
 metodele getFirst si  getLast din cauza ca vroiam sa stocam 
 prenumele si numele si fiindca urma sa derivam aceste clase aveam 
 nevoie de acces la aceste infromatii care erau protected.*/