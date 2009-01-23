/*
In clasa LastFirst, presupunem ca o virgule delimiteaza numele. 
 In ambele clase am tratul cazul in care nu avem spatiu despartitor.*/

package factorymethod3;

class LastFirst extends Namer { //impartim formatul nume prenume
public LastFirst(String s) {
int i = s.indexOf(","); //gasim virgula
if (i > 0) {
//in stanga se aflanumele
last = s.substring(0, i).trim();
//in dreapta se afla prenumele
first = s.substring(i + 1).trim();
}
else {
last = s; // se pune totul in nume
first = ""; // in cazul in care nu avem virgula
}
}
}
